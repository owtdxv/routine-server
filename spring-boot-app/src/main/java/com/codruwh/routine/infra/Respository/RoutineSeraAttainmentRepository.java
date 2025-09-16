package com.codruwh.routine.infra.Respository;

import java.time.LocalDateTime;
import java.util.List;

public interface RoutineSeraAttainmentRepository {
    List<com.codruwh.routine.domain.RoutineSeraAttainment> findByUidAndTimestampBetween(String uid, LocalDateTime start, LocalDateTime end);
    boolean existsByRidAndUidAndTimestampBetween(Integer rid, String uid, LocalDateTime start, LocalDateTime end);
}
