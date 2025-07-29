package com.codruwh.routine.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryDto {
    private Integer categoryId;
    private String value;
}
