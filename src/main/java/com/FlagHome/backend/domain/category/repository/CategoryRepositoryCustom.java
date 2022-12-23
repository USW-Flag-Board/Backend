package com.FlagHome.backend.domain.category.repository;

import com.FlagHome.backend.domain.category.entity.Category;

import java.util.List;

public interface CategoryRepositoryCustom {
    List<Category> findAll();
}
