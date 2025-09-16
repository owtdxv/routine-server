package com.codruwh.routine.infra.Respository;

import java.util.List;
import java.util.Optional;

public interface RoutineSeraRepository {
    List<com.codruwh.routine.domain.RoutineSera> findByUid(String uid);
    Optional<RoutineSera> findById(Integer id);
}
