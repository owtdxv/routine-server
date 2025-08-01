package com.codruwh.routine.infra.repository;

import com.codruwh.routine.domain.WeeklyAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeeklyAttendanceRepository extends JpaRepository<WeeklyAttendance, String> {
}
