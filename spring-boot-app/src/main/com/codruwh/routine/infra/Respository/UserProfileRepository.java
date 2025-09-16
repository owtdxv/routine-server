package com.codruwh.routine.infra.Respository;

import com.codruwh.routine.domain.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
    // 4-10에서 필요 (lux 보상 지급)
    Optional<UserProfile> findByUid(String uid);

}
