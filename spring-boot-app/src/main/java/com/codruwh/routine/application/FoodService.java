package com.codruwh.routine.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codruwh.routine.common.TextNormalizer;
import com.codruwh.routine.controller.dto.FoodSearchResponseDto;
import com.codruwh.routine.infra.repository.FoodSearchProjection;
import com.codruwh.routine.infra.repository.FoodsAllRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodsAllRepository repo;

    /**
     * 음식 이름을 통해 검색
     * @param keyword
     * @param page
     * @param size
     * @return
     */
    @Transactional(readOnly = true)
    public Page<FoodSearchResponseDto> searchByName(String keyword, int page, int size) {
        String norm = TextNormalizer.normForFoodName(keyword);
        
        System.out.println("keyword: "+ keyword + " norm: "+ norm);

        Page<FoodSearchProjection> result = repo.searchByNormLite(norm, PageRequest.of(page, size));
        return result.map(p -> FoodSearchResponseDto.builder()
            .id(p.getId())
            .foodCode(p.getFoodCode())
            .foodName(p.getFoodName())
            .dataTypeName(p.getDataTypeName())
            .dataTypeCode(p.getDataTypeCode())
            .foodNameNorm(p.getFoodNameNorm())
            .servingSize(p.getServingSize())
            .energyKcal(p.getEnergyKcal())
            .waterG(p.getWaterG())
            .proteinG(p.getProteinG())
            .fatG(p.getFatG())
            .carbsG(p.getCarbsG())
            .companyName(p.getCompanyName())
            .productReportNumber(p.getProductReportNumber())
            .manufacturerName(p.getManufacturerName())
            .build());

    }

    @Transactional(readOnly = true)
    public Page<FoodSearchResponseDto> searchFoodOnly(String keyword, int page, int size) {
        String norm = TextNormalizer.normForFoodName(keyword);
        Page<FoodSearchProjection> result =
                repo.searchOnlyFood(norm, PageRequest.of(page, size));
        return result.map(this::toDto);
    }

    @Transactional(readOnly = true)
    public Page<FoodSearchResponseDto> searchProcessedOnly(String keyword, int page, int size) {
        String norm = TextNormalizer.normForFoodName(keyword);
        Page<FoodSearchProjection> result =
                repo.searchOnlyProcessed(norm, PageRequest.of(page, size));
        return result.map(this::toDto);
    }

    private FoodSearchResponseDto toDto(FoodSearchProjection p) {
        return FoodSearchResponseDto.builder()
                .id(p.getId())
                .foodCode(p.getFoodCode())
                .foodName(p.getFoodName())
                .dataTypeName(p.getDataTypeName())
                .dataTypeCode(p.getDataTypeCode())
                .foodNameNorm(p.getFoodNameNorm())
                .servingSize(p.getServingSize())
                .energyKcal(p.getEnergyKcal())
                .waterG(p.getWaterG())
                .proteinG(p.getProteinG())
                .fatG(p.getFatG())
                .carbsG(p.getCarbsG())
                .companyName(p.getCompanyName())
                .productReportNumber(p.getProductReportNumber())
                .manufacturerName(p.getManufacturerName())
                .build();
    }
}
