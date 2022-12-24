package com.FlagHome.backend.domain.category.repository;

import com.FlagHome.backend.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long>, CategoryRepositoryCustom {

}
