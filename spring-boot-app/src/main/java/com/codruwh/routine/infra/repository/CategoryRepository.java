package com.codruwh.routine.infra.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codruwh.routine.domain.Category;
import com.codruwh.routine.domain.UserProfile;
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findById(Integer id);
}
