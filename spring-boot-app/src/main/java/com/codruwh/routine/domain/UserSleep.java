package com.codruwh.routine.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="users_sleep")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSleep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sleep_log_id")
    private Long sleepLogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    private UserProfile userProfile;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    private LocalDateTime starTime;

    private LocalDateTime endTime;

    private Integer sleepDuration; // in minutes
}
