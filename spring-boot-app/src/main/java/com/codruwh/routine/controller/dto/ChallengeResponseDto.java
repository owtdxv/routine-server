package com.codruwh.routine.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChallengeResponseDto {
    private CategoryDto category;
    private String content;
    private Long participant; // 참여자 수는 long 타입이 더 안전합니다.
}
