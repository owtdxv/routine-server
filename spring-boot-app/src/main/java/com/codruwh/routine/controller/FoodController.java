package com.codruwh.routine.controller;

import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codruwh.routine.application.FoodService;
import com.codruwh.routine.controller.dto.FoodSearchResponseDto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/food")
@RequiredArgsConstructor
@Validated
public class FoodController {

    private final FoodService foodService;

    @GetMapping("/search")
    public Page<FoodSearchResponseDto> search(
            @RequestParam @NotBlank String keyword,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size
    ) {
        return foodService.searchByName(keyword, page, size);
    }

    @GetMapping("/search/d")       // 음식(D)만
    public Page<FoodSearchResponseDto> searchFoods(
            @RequestParam @NotBlank String keyword,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size
    ) {
        return foodService.searchFoodOnly(keyword, page, size);
    }

    @GetMapping("/search/p")   // 가공식품(P)만
    public Page<FoodSearchResponseDto> searchProcessed(
            @RequestParam @NotBlank String keyword,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size
    ) {
        return foodService.searchProcessedOnly(keyword, page, size);
    }
}
