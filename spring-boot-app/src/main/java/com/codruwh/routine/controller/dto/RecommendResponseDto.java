package com.codruwh.routine.controller.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class RecommendResponseDto {
    // 클라이언트가 요청한 카테고리 이름 목록
    private List<String> category;
    // 추천된 루틴 DTO 목록
    private List<RecommendedRoutineDto> recommend;
}
