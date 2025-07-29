package com.codruwh.routine.infra.repository;

import com.codruwh.routine.domain.RoutineCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutineCollectionRepository extends JpaRepository<RoutineCollection, Integer> {

}
