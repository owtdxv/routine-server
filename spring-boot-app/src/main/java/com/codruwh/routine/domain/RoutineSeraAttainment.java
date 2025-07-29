package com.codruwh.routine.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "routines_sera_attainment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoutineSeraAttainment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attainmentId; // 로그성 테이블을 위한 별도 PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid", nullable = false)
    private UserProfile userProfile; // FK to users_profile [cite: 106]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", nullable = false, referencedColumnName = "id")
    private RoutineSera routineSera; // FK to routines_sera [cite: 106]

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp; // 달성 시간 [cite: 106]
}
