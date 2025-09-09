package com.codruwh.routine.infra.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codruwh.routine.domain.UserAttainment;

public interface UserAttainmentRepository extends JpaRepository<UserAttainment, Long> {
    List<UserAttainment> findByUserProfileUidAndTimestampBetween(String uid, LocalDateTime start, LocalDateTime end);
    List<UserAttainment> findByUidAndTimestampBetween(String uid, LocalDateTime start, LocalDateTime end);
    boolean existsByRidAndTimestampBetween(Integer rid, LocalDateTime start, LocalDateTime end);
    boolean existsByRidAndUidAndTimestampBetween(Integer rid, String uid, LocalDateTime start, LocalDateTime end);
    void deleteByRidAndUidAndTimestampBetween(Integer rid, String uid, LocalDateTime start, LocalDateTime end);
}
