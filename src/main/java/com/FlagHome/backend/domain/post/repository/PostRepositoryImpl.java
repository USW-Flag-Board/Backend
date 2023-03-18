package com.FlagHome.backend.domain.post.repository;

import com.FlagHome.backend.domain.board.enums.SearchType;
import com.FlagHome.backend.domain.post.controller.dto.LightPostDto;
import com.FlagHome.backend.domain.post.controller.dto.PostDto;
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
    public List<LightPostDto> findMyPostList(String loginId) {
        return jpaQueryFactory
                .select(Projections.constructor(LightPostDto.class,
                    post.id,
                    post.title,
                    post.board.englishName,
                    post.createdAt,
                    post.viewCount,
                    post.likeList.size()))
                .from(post)
                .where(post.member.loginId.eq(loginId))
                .fetch();
    }

    @Override
    public List<PostDto> findBoardWithCondition(String boardName, SearchType searchType, String searchWord) {
        return jpaQueryFactory
                .select(Projections.constructor(PostDto.class,
                        post.id,
                        post.title,
                        post.createdAt,
                        post.board.id,
                        post.member.name,
                        post.viewCount,
                        post.likeList.size(),
                        post.replyList.size()
                ))
                .from(post)
                .where(boardNameCondition(boardName),
                        titleCondition(searchType, searchWord),
                        contentCondition(searchType, searchWord),
                        titleAndContentCondition(searchType, searchWord),
                        userNameCondition(searchType, searchWord),
                        userIdCondition(searchType, searchWord))
                .fetch();
    }

    @Override
    public List<LightPostDto> findTopNPostListByDateAndLike(int postCount) {
        return jpaQueryFactory
                .select(Projections.constructor(LightPostDto.class,
                        post.id,
                        post.title,
                        post.board.englishName,
                        post.createdAt,
                        post.viewCount,
                        post.likeList.size()))
                .from(post)
                .orderBy(
                        post.createdAt.desc(),
                        post.likeList.size().desc()
                )
                .limit(postCount)
                .fetch();
    }

    private BooleanExpression boardNameCondition(String boardName) {
        if(boardName == null)
            return null;

        return post.board.englishName.eq(boardName);
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

    private BooleanExpression userNameCondition(SearchType searchType, String searchWord) {
        if(searchType == null || searchWord == null)
            return null;
        if(searchType != SearchType.USER_NAME)
            return null;

        return post.member.name.eq(searchWord);
    }

    private BooleanExpression userIdCondition(SearchType searchType, String searchWord) {
        if(searchType == null || searchWord == null)
            return null;
        if(searchType != SearchType.LOGIN_ID)
            return null;

        return post.member.loginId.eq(searchWord);
    }
}
