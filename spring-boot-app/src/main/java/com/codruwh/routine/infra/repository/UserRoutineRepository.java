package com.codruwh.routine.infra.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codruwh.routine.domain.UserRoutine;

public interface UserRoutineRepository extends JpaRepository<UserRoutine, Integer> {
    List<UserRoutine> findByUserProfileUid(String uid);
}
