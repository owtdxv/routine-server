package com.codruwh.routine.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categorys")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer categoryId; // PK, auto_increment [cite: 4]

    @Column(name = "value")
    private String value; // 카테고리 값 (e.g., 숙면, 운동) [cite: 4]
}
