package com.FlagHome.backend.domain.post.repository;

import com.FlagHome.backend.domain.search.enums.SearchType;
import com.FlagHome.backend.domain.post.dto.PostDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.FlagHome.backend.domain.post.entity.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PostDto> findBoardWithCondition(String categoryName, SearchType searchType, String searchWord) {
        return jpaQueryFactory
                .select(Projections.constructor(PostDto.class,
                        post.id,
                        post.title,
                        post.createdAt,
                        post.category.id,
                        post.member.name,
                        post.viewCount))
                .from(post)
                .where(categoryCondition(categoryName),
                        titleCondition(searchType, searchWord),
                        contentCondition(searchType, searchWord),
                        titleAndContentCondition(searchType, searchWord),
                        writerCondition(searchType, searchWord))
                .fetch();
    }

    private BooleanExpression categoryCondition(String categoryName) {
        if(categoryName == null)
            return null;

        return post.category.englishName.eq(categoryName);
    }

    private BooleanExpression titleCondition(SearchType searchType, String searchWord) {
        if(searchType == null || searchWord == null)
            return null;
        if(searchType != SearchType.TITLE && searchType != SearchType.TITLE_AND_CONTENT)
            return null;

        return post.title.contains(searchWord);
    }

    private BooleanExpression contentCondition(SearchType searchType, String searchWord) {
        if(searchType == null || searchWord == null)
            return null;
        if(searchType != SearchType.CONTENT && searchType != SearchType.TITLE_AND_CONTENT)
            return null;

        return post.content.contains(searchWord);
    }

    private BooleanExpression titleAndContentCondition(SearchType searchType, String searchWord) {
        if(searchType == null || searchWord == null)
            return null;
        if(searchType != SearchType.TITLE_AND_CONTENT)
            return null;

        return titleCondition(searchType, searchWord).and(contentCondition(searchType, searchWord));
    }

    private BooleanExpression writerCondition(SearchType searchType, String searchWord) {
        if(searchType == null || searchWord == null)
            return null;
        if(searchType != SearchType.WRITER)
            return null;

        return post.member.name.eq(searchWord);
    }
}
