package java.com.codruwh.routine.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codruwh.routine.domain.UserProfile;
public interface CategoryRepository {
    Optional<Category> findById(Integer id);
}
