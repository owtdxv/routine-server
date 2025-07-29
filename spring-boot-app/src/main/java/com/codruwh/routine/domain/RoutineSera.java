package com.codruwh.routine.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "routines_sera")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoutineSera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // PK [cite: 32]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid", nullable = false)
    private UserProfile userProfile; // FK to users_profile [cite: 32]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category; // FK to categorys [cite: 32]

    @Column(name = "content")
    private String content; // 추천 루틴 내용 [cite: 32]
}
