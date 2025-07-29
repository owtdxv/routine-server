package com.codruwh.routine.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "routine_collections")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoutineCollection {

    @Id
    @Column(name = "collection_id")
    private Integer collectionId; // PK

    @Column(name = "title")
    private String title; // 제목

    @Column(name = "sub_title")
    private String subTitle; // 소제목

    @Lob // TEXT 타입 매핑
    @Column(name = "guide", columnDefinition = "TEXT")
    private String guide; // 가이드
}
