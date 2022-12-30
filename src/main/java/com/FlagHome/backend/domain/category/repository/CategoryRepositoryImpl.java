package com.FlagHome.backend.domain.category.repository;

import com.FlagHome.backend.domain.category.entity.Category;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.List;

import static com.FlagHome.backend.domain.category.entity.QCategory.category;

@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Category> findAll(){
        return queryFactory
                .select(category)
                .from(category)
                .where(category.parent.isNull())
                .fetch();
    }

    @Override
    public HashSet<String> findHashSetOfCategoriesName() {
        HashSet<String> categoryNameSet = new HashSet<>();
        List<Category> fetchResult = queryFactory.selectFrom(category).fetch();

        for(Category eachCategory : fetchResult)
            categoryNameSet.add(eachCategory.getEnglishName());

        return categoryNameSet;
    }
}
