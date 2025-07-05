package com.codruwh.routine.infra.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codruwh.routine.domain.FirstLoginStatus;

public interface FirstLoginStatusRespository extends JpaRepository<FirstLoginStatus, UUID>{

}
