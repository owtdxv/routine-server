package com.codruwh.routine.controller.dto;

import lombok.Getter;
import lombok.Builder;

@Getter
@Builder
public class RoutineAddCustomRequestDto {
    private Integer categoryId;
    private String content;
}
