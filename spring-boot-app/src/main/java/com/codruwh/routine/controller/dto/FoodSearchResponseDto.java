package com.codruwh.routine.controller.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class FoodSearchResponseDto {

    private Long id;
    private String foodCode;
    private String foodName;
    private String dataTypeName;
    private String dataTypeCode;
    private String foodNameNorm;
    private String servingSize;
    private BigDecimal energyKcal;
    private BigDecimal waterG;
    private BigDecimal proteinG;
    private BigDecimal fatG;
    private BigDecimal carbsG;
    private String companyName;
    private String productReportNumber;
    private String manufacturerName;
}

