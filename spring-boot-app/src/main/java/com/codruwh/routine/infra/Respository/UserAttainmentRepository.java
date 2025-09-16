package com.codruwh.routine.infra.Respository;

import com.codruwh.routine.domain.UserAttainment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface UserAttainmentRepository extends JpaRepository<UserAttainment, Long> {
    List<UserAttainment> findByUserProfileUidAndTimestampBetween(String uid, LocalDateTime start, LocalDateTime end);
    List<UserAttainment> findByUidAndTimestampBetween(String uid, LocalDateTime start, LocalDateTime end);
    boolean existsByRidAndTimestampBetween(Integer rid, LocalDateTime start, LocalDateTime end);
    boolean existsByRidAndUidAndTimestampBetween(Integer rid, String uid, LocalDateTime start, LocalDateTime end);
    void deleteByRidAndUidAndTimestampBetween(Integer rid, String uid, LocalDateTime start, LocalDateTime end);
}
