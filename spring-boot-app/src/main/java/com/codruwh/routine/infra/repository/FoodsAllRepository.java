package com.codruwh.routine.infra.repository;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import com.codruwh.routine.domain.FoodsAll;

public interface FoodsAllRepository extends JpaRepository<FoodsAll, Long> {

    @Query("""
       SELECT f.id AS id,
              f.foodCode AS foodCode,
              f.foodName AS foodName,
              f.dataTypeName AS dataTypeName,
              f.dataTypeCode AS dataTypeCode,
              f.foodNameNorm AS foodNameNorm,
              f.servingSize AS servingSize,
              f.energyKcal AS energyKcal,
              f.waterG AS waterG,
              f.proteinG AS proteinG,
              f.fatG AS fatG,
              f.carbsG AS carbsG,
              f.companyName AS companyName,
              f.productReportNumber AS productReportNumber,
              f.manufacturerName AS manufacturerName
       FROM FoodsAll f
       WHERE f.foodNameNorm LIKE CONCAT('%', :norm, '%')
       ORDER BY f.foodName
       """)
       Page<FoodSearchProjection> searchByNormLite(@Param("norm") String norm, Pageable pageable);

       @Query("""
       SELECT f.id AS id,
              f.foodCode AS foodCode,
              f.foodName AS foodName,
              f.dataTypeName AS dataTypeName,
              f.dataTypeCode AS dataTypeCode,
              f.foodNameNorm AS foodNameNorm,
              f.servingSize AS servingSize,
              f.energyKcal AS energyKcal,
              f.waterG AS waterG,
              f.proteinG AS proteinG,
              f.fatG AS fatG,
              f.carbsG AS carbsG,
              f.companyName AS companyName,
              f.productReportNumber AS productReportNumber,
              f.manufacturerName AS manufacturerName
       FROM FoodsAll f
       WHERE f.dataTypeCode = 'D'
         AND f.foodNameNorm LIKE CONCAT('%', :norm, '%')
       ORDER BY f.foodName
       """)
       Page<FoodSearchProjection> searchOnlyFood(@Param("norm") String norm, Pageable pageable);

       @Query("""
       SELECT f.id AS id,
              f.foodCode AS foodCode,
              f.foodName AS foodName,
              f.dataTypeName AS dataTypeName,
              f.dataTypeCode AS dataTypeCode,
              f.foodNameNorm AS foodNameNorm,
              f.servingSize AS servingSize,
              f.energyKcal AS energyKcal,
              f.waterG AS waterG,
              f.proteinG AS proteinG,
              f.fatG AS fatG,
              f.carbsG AS carbsG,
              f.companyName AS companyName,
              f.productReportNumber AS productReportNumber,
              f.manufacturerName AS manufacturerName
       FROM FoodsAll f
       WHERE f.dataTypeCode = 'P'
         AND f.foodNameNorm LIKE CONCAT('%', :norm, '%')
       ORDER BY f.foodName
       """)
       Page<FoodSearchProjection> searchOnlyProcessed(@Param("norm") String norm, Pageable pageable);
}
