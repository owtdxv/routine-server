package com.codruwh.routine.infra.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codruwh.routine.domain.User;

public interface UserRepository extends JpaRepository<User, UUID> {
}
