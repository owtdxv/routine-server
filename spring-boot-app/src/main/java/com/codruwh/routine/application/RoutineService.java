package com.codruwh.routine.application;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codruwh.routine.controller.dto.AllCollectionsResponseDto;
import com.codruwh.routine.controller.dto.AllRoutinesResponseDto;
import com.codruwh.routine.controller.dto.CategoryDto;
import com.codruwh.routine.controller.dto.CollectionDetailDto;
import com.codruwh.routine.controller.dto.RecommendResponseDto;
import com.codruwh.routine.controller.dto.RecommendedRoutineDto;
import com.codruwh.routine.controller.dto.RoutineDto;
import com.codruwh.routine.domain.Routine;
import com.codruwh.routine.domain.RoutineCollection;
import com.codruwh.routine.domain.RoutineCollectionMapper;
import com.codruwh.routine.infra.repository.RoutineCollectionMapperRepository;
import com.codruwh.routine.infra.repository.RoutineCollectionRepository;
import com.codruwh.routine.infra.repository.RoutineRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoutineService {

  private static final List<String> ALL_CATEGORIES = Arrays.asList("수면", "운동", "영양소", "햇빛", "사회적 유대감");
  private static final int TOTAL_RECOMMEND_COUNT = 10;

  private final RoutineRepository routineRepository;
  private final RoutineCollectionRepository routineCollectionRepository;
  private final RoutineCollectionMapperRepository routineCollectionMapperRepository;

    /**
     * 요청된 카테고리에 가중치를 부여하여 총 10개의 루틴을 추천합니다.
     * 5개 모든 카테고리가 최소 1개 이상 포함됩니다.
     *
     * @param requestedCategoryNames 추천받고 싶은 카테고리 이름 목록
     * @return 추천 루틴 정보가 담긴 DTO
     */
    @Transactional(readOnly = true)
    public RecommendResponseDto getRecommendedRoutines(List<String> requestedCategoryNames) {
      // 1. DB 효율을 위해 모든 카테고리의 루틴을 한 번에 조회 후, 카테고리별로 그룹핑합니다.
      List<Routine> allRoutines = routineRepository.findRoutinesByCategoryNames(ALL_CATEGORIES);
      Map<String, List<Routine>> routinesByCategory = allRoutines.stream()
              .collect(Collectors.groupingBy(routine -> routine.getCategory().getValue()));

      List<Routine> resultList = new ArrayList<>();

      // 2. [보장 단계] 5개 모든 카테고리에서 루틴을 1개씩 무작위로 추출합니다.
      for (String categoryName : ALL_CATEGORIES) {
          List<Routine> routinesInCurrentCategory = routinesByCategory.getOrDefault(categoryName, new ArrayList<>());
          if (!routinesInCurrentCategory.isEmpty()) {
              Collections.shuffle(routinesInCurrentCategory);
              // 0번 인덱스의 루틴을 결과에 추가하고, 중복 추출을 막기 위해 리스트에서 제거합니다.
              resultList.add(routinesInCurrentCategory.remove(0));
          }
      }

      // 3. [가중치 부여 단계] 남은 자리를 '요청된 카테고리'의 루틴으로만 채웁니다.
      List<Routine> priorityPool = new ArrayList<>();
      for (String categoryName : requestedCategoryNames) {
          // '보장 단계'에서 사용하고 남은 루틴들을 우선순위 풀에 추가합니다.
          priorityPool.addAll(routinesByCategory.getOrDefault(categoryName, new ArrayList<>()));
      }
      Collections.shuffle(priorityPool);

      int remainingSlots = TOTAL_RECOMMEND_COUNT - resultList.size();
      if (remainingSlots > 0 && !priorityPool.isEmpty()) {
          resultList.addAll(priorityPool.stream().limit(remainingSlots).collect(Collectors.toList()));
      }

      // 4. 최종 결과 리스트를 다시 섞어 순서를 예측할 수 없게 합니다.
      Collections.shuffle(resultList);

      // 5. 최종 결과를 DTO로 변환하여 반환합니다.
      List<RecommendedRoutineDto> dtoList = resultList.stream()
              .map(routine -> RecommendedRoutineDto.builder()
                      .rid(routine.getRid())
                      .content(routine.getContent())
                      .category(CategoryDto.builder()
                              .categoryId(routine.getCategory().getCategoryId())
                              .value(routine.getCategory().getValue())
                              .build())
                      .build())
              .collect(Collectors.toList());

      return RecommendResponseDto.builder()
              .category(requestedCategoryNames)
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
}
