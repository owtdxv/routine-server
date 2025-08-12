package com.codruwh.routine.infra.repository;

import java.math.BigDecimal;

public interface FoodSearchProjection {
    Long getId();
    String getFoodCode();
    String getFoodName();
    String getDataTypeName();
    String getDataTypeCode();
    String getFoodNameNorm();
    String getServingSize();
    BigDecimal getEnergyKcal();
    BigDecimal getWaterG();
    BigDecimal getProteinG();
    BigDecimal getFatG();
    BigDecimal getCarbsG();
    String getCompanyName();
    String getProductReportNumber();
    String getManufacturerName();
}
