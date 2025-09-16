package com.codruwh.routine.infra.Respository;

import com.codruwh.routine.domain.Category;

import java.util.Optional;

public interface CategoryRepository {
    Optional<Category> findById(Integer id);
}
