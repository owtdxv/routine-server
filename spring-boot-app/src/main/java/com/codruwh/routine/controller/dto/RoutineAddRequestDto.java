package com.codruwh.routine.controller.dto;

import lombok.Data;
import java.util.List;

@Data
public class RoutineAddRequestDto {
    private List<Integer> rid; // 루틴의 id값들
}