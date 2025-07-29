package com.codruwh.routine.controller.dto;


import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class StatisticsResponseDto {
    private LocalDate startDay;
    private LocalDate endDay;
    private List<RoutineStatisticsDto> routines;
}
