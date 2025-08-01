package com.codruwh.routine.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AttendanceCheckResponseDto {
    // 주간 개근 완료 여부 플래그
    private boolean completed;
}
