package com.codruwh.routine.infra.Respository;

import com.codruwh.routine.domain.Routine;
import com.codruwh.routine.domain.UserRoutine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRoutineRepository extends JpaRepository<UserRoutine, Integer> {
    List<UserRoutine> findByUserProfileUid(String uid);
    List<Routine> findByIdIn(List<Integer> ids);
    List<UserRoutine> findByUid(String uid);
    Optional<UserRoutine> findById(Integer id);
}
