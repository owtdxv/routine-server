package com.codruwh.routine.controller.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class AllRoutinesResponseDto {
    private List<RoutineDto> routines;
}
