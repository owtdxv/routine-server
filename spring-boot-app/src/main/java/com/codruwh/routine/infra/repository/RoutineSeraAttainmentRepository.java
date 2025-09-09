package java.com.codruwh.routine.infra.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.codruwh.routine.domain.UserProfile;

import java.time.LocalDateTime;
import java.util.List;

public interface RoutineSeraAttainmentRepository {
    List<com.codruwh.routine.domain.RoutineSeraAttainment> findByUidAndTimestampBetween(String uid, LocalDateTime start, LocalDateTime end);
    boolean existsByRidAndUidAndTimestampBetween(Integer rid, String uid, LocalDateTime start, LocalDateTime end);
}
