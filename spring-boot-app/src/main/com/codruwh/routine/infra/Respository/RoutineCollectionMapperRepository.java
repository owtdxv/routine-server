package com.codruwh.routine.infra.Respository;

import com.codruwh.routine.domain.RoutineCollectionMapper;
import com.codruwh.routine.domain.idClass.RoutineCollectionMapperId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoutineCollectionMapperRepository extends JpaRepository<RoutineCollectionMapper, RoutineCollectionMapperId> {

    /**
     * 모든 매퍼 정보를 관련된 Routine, Category 정보와 함께 한 번의 쿼리로 조회합니다.
     * N+1 문제를 방지하기 위해 JOIN FETCH를 사용합니다.
     * @return 모든 상세 정보가 포함된 RoutineCollectionMapper 리스트
     */
    @Query("SELECT m FROM RoutineCollectionMapper m " +
           "JOIN FETCH m.routine r " +
           "JOIN FETCH r.category")
    List<RoutineCollectionMapper> findAllWithDetails();
}
