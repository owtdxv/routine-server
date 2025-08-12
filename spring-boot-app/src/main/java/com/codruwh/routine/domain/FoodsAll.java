package com.codruwh.routine.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "foods_all")
public class FoodsAll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="food_code", nullable=false)
    private String foodCode;

    @Column(name="food_name", nullable=false)
    private String foodName;

    @Column(name = "data_type_code")
    private String dataTypeCode; 

    @Column(name="data_type_name")
    private String dataTypeName;

    @Column(name="food_name_norm")
    private String foodNameNorm;     // 정규화 컬럼

    @Column(name="serving_size") 
    private String servingSize;

    @Column(name="energy_kcal")  
    private BigDecimal energyKcal;

    @Column(name="water_g")     
    private BigDecimal waterG;

    @Column(name="protein_g")    
    private BigDecimal proteinG;

    @Column(name="fat_g")
    private BigDecimal fatG;

    @Column(name="carbs_g")
    private BigDecimal carbsG;

    @Column(name = "company_name")
    private String companyName;           // 음식DB 전용

    @Column(name = "product_report_no")
    private String productReportNumber;   // 가공식품 전용

    @Column(name = "manufacturer_name")
    private String manufacturerName;      // 가공식품 전용

    // 나머지 칼럼은 생략 가능 (읽기 전용 목적)
    @Column(name="created_at")  private LocalDate createdAt;
    @Column(name="reference_date") private LocalDate referenceDate;
}
