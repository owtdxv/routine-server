package com.codruwh.routine.controller.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.Map;

@Getter
@Builder
public class RoutineStatisticsDto {
    private Integer id;
    private String content;
    private Map<String, Boolean> attainments;
}
