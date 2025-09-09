package com.codruwh.routine.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codruwh.routine.domain.UserProfile;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
    // 4-10에서 필요 (lux 보상 지급)
    Optional<UserProfile> findByUid(String uid);

}
