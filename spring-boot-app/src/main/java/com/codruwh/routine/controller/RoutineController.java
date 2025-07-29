package com.codruwh.routine.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codruwh.routine.application.RoutineService;
import com.codruwh.routine.common.ApiException;
import com.codruwh.routine.controller.dto.RecommendResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Routine", description = "루틴 관련 API")
@RestController
@RequestMapping("/routine")
@RequiredArgsConstructor
public class RoutineController {

  /**
   * 루틴 추천/생성 시 허용된 카테고리 목록
   */
  private static final List<String> ALLOWED_CATEGORIES =
            Arrays.asList("수면", "운동", "영양소", "햇빛", "사회적유대감");

  private final RoutineService routineService;

  @Operation(
            summary = "추천 루틴 생성",
            description = "하나 이상의 카테고리를 지정하면 해당 카테고리에 높은 가중치를 부여하여 10개의 루틴을 추천해 줍니다."
    )
    @GetMapping("/recommend")
    public ResponseEntity<RecommendResponseDto> getRecommendedRoutines(
            @Parameter(description = "추천받고 싶은 카테고리 이름. 여러 개 지정 가능.", required = true, example = "sleep")
            @RequestParam("category") List<String> categories
    ) {
        // 유효성 검증: 카테고리 파라미터가 비어있는지 확인합니다.
        if (categories == null || categories.isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "하나 이상의 카테고리를 지정해야 합니다.");
        }

        // 유효성 검증: 요청된 각 카테고리 이름이 유효한 카테고리 인지 확인합니다
        for (String categoryName : categories) {
            if (!ALLOWED_CATEGORIES.contains(categoryName)) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "유효하지 않은 카테고리 이름입니다: " + categoryName);
            }
        }

        // 서비스 로직을 호출하여 추천 루틴 데이터를 가져옵니다.
        RecommendResponseDto responseDto = routineService.getRecommendedRoutines(categories);

        return ResponseEntity.ok(responseDto);
    }

}
