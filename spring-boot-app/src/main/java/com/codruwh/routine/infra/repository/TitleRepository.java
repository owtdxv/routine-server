package com.codruwh.routine.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codruwh.routine.domain.Title;

public interface TitleRepository extends JpaRepository<Title, Integer>{

}
