package com.codruwh.routine.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codruwh.routine.domain.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {

}
