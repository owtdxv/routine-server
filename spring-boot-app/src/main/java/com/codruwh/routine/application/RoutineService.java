package com.codruwh.routine.application;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codruwh.routine.controller.dto.CategoryDto;
import com.codruwh.routine.controller.dto.RecommendResponseDto;
import com.codruwh.routine.controller.dto.RecommendedRoutineDto;
import com.codruwh.routine.domain.Routine;
import com.codruwh.routine.infra.repository.RoutineRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoutineService {

  private final RoutineRepository routineRepository;
  private static final List<String> ALL_CATEGORIES = Arrays.asList("수면", "운동", "영양소", "햇빛", "사회적 유대감");
  private static final int TOTAL_RECOMMEND_COUNT = 10;

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
}
