package java.com.codruwh.routine.infra.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.codruwh.routine.domain.UserProfile;
public interface RoutineSeraRepository {
    List<com.codruwh.routine.domain.RoutineSera> findByUid(String uid);
    Optional<RoutineSera> findById(Integer id);
}
