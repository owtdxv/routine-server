package com.codruwh.routine.controller.dto;

import com.google.auto.value.AutoValue.Builder;

import lombok.Getter;

@Getter
@Builder
public class RoutineAddCustomRequestDto {
    private Integer categoryId;
    private String content;
}
