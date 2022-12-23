package com.FlagHome.backend.v1.category.repository;

import com.FlagHome.backend.v1.category.entity.Category;

import java.util.List;

public interface CategoryRepositoryCustom {
    List<Category> findAll();
}
