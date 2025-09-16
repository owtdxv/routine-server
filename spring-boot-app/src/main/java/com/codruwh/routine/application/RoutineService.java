package com.codruwh.routine.application;

import com.codruwh.routine.controller.dto.PersonalRoutineItemDto;
import com.codruwh.routine.controller.dto.PersonalRoutineResponseDto;
import com.codruwh.routine.controller.dto.RecommendRoutineItemDto;
import com.codruwh.routine.controller.dto.RecommendRoutineResponseDto;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import com.codruwh.routine.domain.*;
import com.codruwh.routine.infra.repository.*;

import org.springframework.stereotype.Service;

import com.codruwh.routine.controller.dto.AllCollectionsResponseDto;
import com.codruwh.routine.controller.dto.AllRoutinesResponseDto;
import com.codruwh.routine.controller.dto.CategoryDto;
import com.codruwh.routine.controller.dto.CollectionDetailDto;
import com.codruwh.routine.controller.dto.RecommendedRoutineDto;
import com.codruwh.routine.controller.dto.RoutineDto;
import com.codruwh.routine.domain.Routine;
import com.codruwh.routine.domain.RoutineCollection;
import com.codruwh.routine.domain.RoutineCollectionMapper;
import com.codruwh.routine.infra.repository.RoutineCollectionMapperRepository;
import com.codruwh.routine.infra.repository.RoutineCollectionRepository;
import com.codruwh.routine.infra.repository.RoutineRepository;
import com.codruwh.routine.controller.dto.RoutineAddRequestDto;
import com.codruwh.routine.domain.UserRoutine;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class RoutineService {

    private static final List<String> ALL_CATEGORIES = Arrays.asList("수면", "운동", "영양소", "햇빛", "사회적유대감");
    private static final int TOTAL_RECOMMEND_COUNT = 10;

    private final RoutineRepository routineRepository;
    private final RoutineCollectionRepository routineCollectionRepository;
    private final RoutineCollectionMapperRepository routineCollectionMapperRepository;
    private final UserRoutineRepository userRoutineRepository;
    private final UserAttainmentRepository userAttainmentRepository;
    private final CategoryRepository categoryRepository;
    private final RoutineSeraRepository routineSeraRepository;
    private final RoutineSeraAttainmentRepository routineSeraAttainmentRepository;
    private final UserProfileRepository userProfileRepository;

    /**
     * 요청된 카테고리에 가중치를 부여하여 총 10개의 루틴을 추천합니다.
     * 5개 모든 카테고리가 최소 1개 이상 포함됩니다.
     *
     * @param requestedCategoryNames 추천받고 싶은 카테고리 이름 목록
     * @return 추천 루틴 정보가 담긴 DTO
     */
    @Transactional(readOnly = true)
    public RecommendResponseDto getRecommendedRoutines(List<String> requestedCategoryNames) {
        // 입력 정제
        List<String> requested = Optional.ofNullable(requestedCategoryNames)
                .orElse(Collections.emptyList())
                .stream().filter(ALL_CATEGORIES::contains).distinct().toList();

        // 전 카테고리 조회/그룹
        List<Routine> all = routineRepository.findRoutinesByCategoryNames(ALL_CATEGORIES);
        Map<String, List<Routine>> byCat = all.stream()
                .collect(Collectors.groupingBy(r -> r.getCategory().getValue()));

        // 카테고리별 데크 준비(셔플 후 pop)
        Map<String, Deque<Routine>> deck = new HashMap<>();
        for (String cat : ALL_CATEGORIES) {
            List<Routine> list = new ArrayList<>(byCat.getOrDefault(cat, List.of()));
            Collections.shuffle(list);
            deck.put(cat, new ArrayDeque<>(list));
        }

        List<Routine> result = new ArrayList<>(TOTAL_RECOMMEND_COUNT);

        // 1) 전 카테고리 최소 1개 보장
        for (String cat : ALL_CATEGORIES) {
            Deque<Routine> q = deck.get(cat);
            if (q != null && !q.isEmpty()) result.add(q.pollFirst());
        }

        // 2) 요청 가중치: 라운드로빈 + 무작위 시작 (+ 선택적 상한)
        int remaining = TOTAL_RECOMMEND_COUNT - result.size();
        Map<String,Integer> taken = new HashMap<>();
        for (Routine r : result) taken.merge(r.getCategory().getValue(), 1, Integer::sum);

        List<String> order = new ArrayList<>(requested);
        if (!order.isEmpty()) {
            int start = new Random().nextInt(order.size()); // 편향 제거
            Collections.rotate(order, -start);
        }

        final int MAX_PER_CATEGORY = 3; // 선택: 한 카테고리 총 3개 초과 금지(과다 편중 방지)

        int idx = 0;
        while (remaining > 0 && !order.isEmpty()) {
            String cat = order.get(idx % order.size());
            Deque<Routine> q = deck.get(cat);
            int cur = taken.getOrDefault(cat, 0);
            if (q != null && !q.isEmpty() && cur < MAX_PER_CATEGORY) {
                result.add(q.pollFirst());
                taken.put(cat, cur + 1);
                remaining--;
                idx++;
            } else {
                order.remove(cat); // 고갈/상한 도달 시 제외
            }
        }

        // 3) 아직 남으면 비요청에서 보충(상한 고려)
        if (remaining > 0) {
            List<String> others = ALL_CATEGORIES.stream()
                    .filter(c -> !requested.contains(c)).toList();
            int j = 0;
            while (remaining > 0 && !others.isEmpty()) {
                String cat = others.get(j % others.size());
                Deque<Routine> q = deck.get(cat);
                int cur = taken.getOrDefault(cat, 0);
                if (q != null && !q.isEmpty() && cur < MAX_PER_CATEGORY) {
                    result.add(q.pollFirst());
                    taken.put(cat, cur + 1);
                    remaining--;
                }
                j++;
            }
        }

        Collections.shuffle(result);

        // DTO 매핑
        List<RecommendedRoutineDto> dtoList = result.stream()
                .map(r -> RecommendedRoutineDto.builder()
                        .rid(r.getRid())
                        .content(r.getContent())
                        .category(CategoryDto.builder()
                                .categoryId(r.getCategory().getCategoryId())
                                .value(r.getCategory().getValue())
                                .build())
                        .build())
                .toList();

        return RecommendResponseDto.builder()
                .category(requested)
                .recommend(dtoList)
                .build();
    }

    /**
     * routines 테이블에 저장된 모든 루틴 정보를 조회합니다
     * @return 모든 루틴 정보
     */
    @Transactional(readOnly = true) // 읽기 전용
    public AllRoutinesResponseDto getAllRoutines() {
        List<Routine> allRoutines = routineRepository.findAll();

        List<RoutineDto> dtoList = allRoutines.stream()
                .map(routine -> RoutineDto.builder()
                        .rid(routine.getRid())
                        .content(routine.getContent())
                        .category(CategoryDto.builder()
                                .categoryId(routine.getCategory().getCategoryId())
                                .value(routine.getCategory().getValue())
                                .build())
                        .build())
                .collect(Collectors.toList());

        return AllRoutinesResponseDto.builder()
                .routines(dtoList)
                .build();
    }

    /**
     * 모든 루틴 컬렉션 정보를 반환합니다.
     * @return 루틴 컬렉션 정보
     */
    @Transactional(readOnly = true)
    public AllCollectionsResponseDto getAllRoutineCollections() {
        // 1. 모든 루틴 컬렉션(패키지)을 조회합니다. (1번의 쿼리)
        List<RoutineCollection> allCollections = routineCollectionRepository.findAll();

        // 2. 모든 매퍼와 관련 루틴 정보를 한 번에 조회합니다. (1번의 쿼리)
        List<RoutineCollectionMapper> allMappers = routineCollectionMapperRepository.findAllWithDetails();

        // 3. 조회된 매퍼들을 collectionId를 기준으로 그룹화하여 Map으로 만듭니다. (메모리에서 처리)
        // Key: collectionId, Value: 해당 collection에 속한 RoutineDto 리스트
        Map<Integer, List<RoutineDto>> routinesByCollectionId = allMappers.stream()
                .collect(Collectors.groupingBy(
                        mapper -> mapper.getRoutineCollection().getCollectionId(),
                        Collectors.mapping(
                                mapper -> {
                                    var routine = mapper.getRoutine();
                                    return RoutineDto.builder()
                                            .rid(routine.getRid())
                                            .content(routine.getContent())
                                            .category(CategoryDto.builder()
                                                    .categoryId(routine.getCategory().getCategoryId())
                                                    .value(routine.getCategory().getValue())
                                                    .build())
                                            .build();
                                },
                                Collectors.toList()
                        )
                ));

        // 4. 컬렉션 목록을 순회하며, 위에서 만든 Map을 이용해 각 컬렉션에 루틴 목록을 채워넣습니다.
        List<CollectionDetailDto> resultDtoList = allCollections.stream()
                .map(collection -> {
                    CollectionDetailDto dto = CollectionDetailDto.builder()
                            .collectionId(collection.getCollectionId())
                            .title(collection.getTitle())
                            .subTitle(collection.getSubTitle())
                            .guide(collection.getGuide())
                            .build();
                    // Map에서 해당 collectionId의 루틴 리스트를 찾아 설정합니다.
                    dto.setRoutines(routinesByCollectionId.get(collection.getCollectionId()));
                    return dto;
                })
                .collect(Collectors.toList());

        // 5. 최종 응답 객체를 빌드하여 반환합니다.
        return AllCollectionsResponseDto.builder()
                .collections(resultDtoList)
                .build();
    }

    @Transactional
    public void addRoutinesToUser(String uid, RoutineAddRequestDto request) {
        // 1. routines 테이블에서 해당 rid들의 정보를 조회
        List<Routine> routines = routineRepository.findByIdIn(request.getRid());

        // 2. 요청된 모든 루틴이 존재하는지 확인
        if (routines.size() != request.getRid().size()) {
            throw new IllegalArgumentException("존재하지 않는 루틴 ID가 포함되어 있습니다.");
        }

        // 3. 각 루틴 정보를 users_routine 테이블에 저장
        for (Routine routine : routines) {
            UserRoutine userRoutine = UserRoutine.builder()
                    .uid(uid)
                    .categoryId(routine.getCategoryId())
                    .content(routine.getContent())
                    .notification(null) // 일단 null로 저장
                    .build();

            userRoutineRepository.save(userRoutine);
        }
    }
    @Transactional
    public void addCustomRoutineToUser(String uid, RoutineAddCustomRequestDto request) {
        // 1. 입력값 검증
        if (request.getCategoryId() == null || request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("카테고리 ID와 루틴 내용은 필수입니다.");
        }

        // 2. 카테고리 존재 여부 확인 (선택사항)
        // Category category = categoryRepository.findById(request.getCategoryId())
        //     .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

        // 3. 사용자 직접 작성 루틴을 users_routine 테이블에 저장
        UserRoutine userRoutine = UserRoutine.builder()
                .uid(uid)
                .categoryId(request.getCategoryId())
                .content(request.getContent().trim())
                .notification(null) // 일단 null로 저장
                .build();

        userRoutineRepository.save(userRoutine);
    }
    public PersonalRoutineResponseDto getPersonalRoutines(String uid) {
        // 1. 사용자의 루틴들을 조회
        List<UserRoutine> userRoutines = userRoutineRepository.findByUid(uid);

        // 2. 오늘 날짜의 달성 기록들을 조회
        LocalDate today = LocalDate.now();
        List<com.codruwh.routine.domain.UserAttainment> todayAttainments = userAttainmentRepository
                .findByUidAndTimestampBetween(
                        uid,
                        today.atStartOfDay(),
                        today.plusDays(1).atStartOfDay()
                );

        // 3. 달성된 루틴 ID들을 Set으로 변환 (빠른 조회를 위해)
        Set<Integer> completedRoutineIds = todayAttainments.stream()
                .map(attainment -> attainment.getRid()) // UserAttainment의 rid 필드
                .collect(Collectors.toSet());

        // 4. UserRoutine을 DTO로 변환
        List<PersonalRoutineItemDto> routineItems = userRoutines.stream()
                .map(userRoutine -> {
                    // 카테고리 정보 가져오기
                    Category category = categoryRepository.findById(userRoutine.getCategoryId())
                            .orElse(null);

                    CategoryDto categoryDto = null;
                    if (category != null) {
                        categoryDto = CategoryDto.builder()
                                .categoryId(category.getId())
                                .value(category.getValue()) // 또는 category.getName()
                                .build();
                    }

                    // 달성 여부 확인
                    boolean isComplete = completedRoutineIds.contains(userRoutine.getId());

                    return PersonalRoutineItemDto.builder()
                            .id(userRoutine.getId())
                            .category(categoryDto)
                            .content(userRoutine.getContent())
                            .notification(userRoutine.getNotification())
                            .complete(isComplete)
                            .build();
                })
                .collect(Collectors.toList());

        return PersonalRoutineResponseDto.builder()
                .routines(routineItems)
                .build();
    }
    @Transactional
    public void updateRoutine(Integer id, RoutineUpdateRequestDto request, String tokenUid) {
        UserRoutine userRoutine = userRoutineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("루틴을 찾을 수 없습니다."));

        // 본인의 루틴만 수정 가능
        if (!userRoutine.getUid().equals(tokenUid)) {
            throw new IllegalArgumentException("본인의 루틴만 수정할 수 있습니다.");
        }

        // 수정
        userRoutine.setCategoryId(request.getCategoryId());
        userRoutine.setContent(request.getContent());

        userRoutineRepository.save(userRoutine);
    }
    @Transactional
    public void deleteRoutine(Integer id, String tokenUid) {
        UserRoutine userRoutine = userRoutineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("루틴을 찾을 수 없습니다."));

        // 본인의 루틴만 삭제 가능
        if (!userRoutine.getUid().equals(tokenUid)) {
            throw new IllegalArgumentException("본인의 루틴만 삭제할 수 있습니다.");
        }

        userRoutineRepository.delete(userRoutine);
    }

    @Transactional
    public void checkRoutineAttainment(String uid, Integer routineId) {
        // 루틴 존재 및 소유자 확인
        UserRoutine userRoutine = userRoutineRepository.findById(routineId)
                .orElseThrow(() -> new IllegalArgumentException("루틴을 찾을 수 없습니다."));

        if (!userRoutine.getUid().equals(uid)) {
            throw new IllegalArgumentException("본인의 루틴만 체크할 수 있습니다.");
        }

        // 오늘 이미 체크했는지 확인
        LocalDate today = LocalDate.now();
        boolean alreadyChecked = userAttainmentRepository.existsByRidAndUidAndTimestampBetween(
                routineId, uid, today.atStartOfDay(), today.plusDays(1).atStartOfDay());

        if (alreadyChecked) {
            throw new IllegalArgumentException("이미 체크된 루틴입니다.");
        }

        // 달성 기록 저장
        UserAttainment attainment = UserAttainment.builder()
                .uid(uid)
                .rid(routineId)
                .timestamp(LocalDateTime.now())
                .build();

        userAttainmentRepository.save(attainment);
    }

    @Transactional
    public void uncheckRoutineAttainment(String uid, Integer routineId) {
        // 오늘 날짜의 달성 기록 삭제
        LocalDate today = LocalDate.now();
        userAttainmentRepository.deleteByRidAndUidAndTimestampBetween(
                routineId, uid, today.atStartOfDay(), today.plusDays(1).atStartOfDay());
    }

    public RecommendRoutineResponseDto getRecommendRoutineResp(String uid) {
        // Sera 추천 루틴들 조회 (RoutineSera 테이블에서)
        List<RoutineSera> seraRoutines = routineSeraRepository.findByUid(uid);

        // 오늘 날짜의 Sera 루틴 달성 기록들 조회
        LocalDate today = LocalDate.now();
        List<com.codruwh.routine.domain.RoutineSeraAttainment> todayAttainments = routineSeraAttainmentRepository
                .findByUidAndTimestampBetween(uid, today.atStartOfDay(), today.plusDays(1).atStartOfDay());

        Set<Integer> completedRoutineIds = todayAttainments.stream()
                .map(RoutineSeraAttainment::getRid)
                .collect(Collectors.toSet());

        List<RecommendRoutineItemDto> routineItems = seraRoutines.stream()
                .map(seraRoutine -> {
                    Category category = categoryRepository.findById(seraRoutine.getCategoryId())
                            .orElse(null);

                    CategoryDto categoryDto = null;
                    if (category != null) {
                        categoryDto = CategoryDto.builder()
                                .categoryId(category.getId())
                                .value(category.getValue())
                                .build();
                    }

                    boolean isComplete = completedRoutineIds.contains(seraRoutine.getId());

                    return RecommendRoutineItemDto.builder()
                            .id(seraRoutine.getId())
                            .category(categoryDto)
                            .content(seraRoutine.getContent())
                            .complete(isComplete)
                            .build();
                })
                .collect(Collectors.toList());

        return RecommendRoutineResponseDto.builder()
                .routines(routineItems)
                .build();
    }
    public RecommendRoutineResponseDto getRecommendRoutines(String uid) {
        // Sera 추천 루틴들 조회 (RoutineSera 테이블에서)
        List<RoutineSera> seraRoutines = routineSeraRepository.findByUid(uid);

        // 오늘 날짜의 Sera 루틴 달성 기록들 조회
        LocalDate today = LocalDate.now();
        List<RoutineSeraAttainment> todayAttainments = routineSeraAttainmentRepository
                .findByUidAndTimestampBetween(uid, today.atStartOfDay(), today.plusDays(1).atStartOfDay());

        Set<Integer> completedRoutineIds = todayAttainments.stream()
                .map(RoutineSeraAttainment::getRid)
                .collect(Collectors.toSet());

        List<RecommendRoutineItemDto> routineItems = seraRoutines.stream()
                .map(seraRoutine -> {
                    com.codruwh.routine.domain.Category category = categoryRepository.findById(seraRoutine.getCategoryId())
                            .orElse(null);

                    CategoryDto categoryDto = null;
                    if (category != null) {
                        categoryDto = CategoryDto.builder()
                                .categoryId(category.getId())
                                .value(category.getValue())
                                .build();
                    }

                    boolean isComplete = completedRoutineIds.contains(seraRoutine.getId());

                    return RecommendRoutineItemDto.builder()
                            .id(seraRoutine.getId())
                            .category(categoryDto)
                            .content(seraRoutine.getContent())
                            .complete(isComplete)
                            .build();
                })
                .collect(Collectors.toList());

        return RecommendRoutineResponseDto.builder()
                .routines(routineItems)
                .build();
    }

    @Transactional
    public void checkSeraRoutineAttainment(String uid, Integer routineId) {
        // Sera 루틴 존재 및 소유자 확인
        RoutineSera seraRoutine = routineSeraRepository.findById(routineId)
                .orElseThrow(() -> new IllegalArgumentException("Sera 루틴을 찾을 수 없습니다."));

        if (!seraRoutine.getUid().equals(uid)) {
            throw new IllegalArgumentException("본인의 Sera 루틴만 체크할 수 있습니다.");
        }

        // 오늘 이미 체크했는지 확인
        LocalDate today = LocalDate.now();
        boolean alreadyChecked = routineSeraAttainmentRepository.existsByRidAndUidAndTimestampBetween(
                routineId, uid, today.atStartOfDay(), today.plusDays(1).atStartOfDay());

        if (alreadyChecked) {
            throw new IllegalArgumentException("이미 체크된 Sera 루틴입니다.");
        }

        // 달성 기록 저장
        RoutineSeraAttainment attainment = RoutineSeraAttainment.builder()
                .uid(uid)
                .rid(routineId)
                .timestamp(LocalDateTime.now())
                .build();

        routineSeraAttainmentRepository.save(attainment);

        // 사용자에게 추가 lux 제공
        UserProfile userProfile = userProfileRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        final int SERA_ROUTINE_LUX_BONUS = 10; // 보너스 lux 양
        userProfile.setLux(userProfile.getLux() + SERA_ROUTINE_LUX_BONUS);
        userProfileRepository.save(userProfile);
    }

}
