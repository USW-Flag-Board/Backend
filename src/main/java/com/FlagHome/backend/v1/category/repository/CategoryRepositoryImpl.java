package com.FlagHome.backend.v1.category.repository;

import com.FlagHome.backend.v1.category.entity.Category;
import com.FlagHome.backend.v1.category.entity.QCategory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Category> findAll(){
        QCategory category = QCategory.category;
        List<Category> result = queryFactory
                .select(category)
                .from(category)
                .where(category.parent.isNull())
                .fetch();

        return result;
    }
}