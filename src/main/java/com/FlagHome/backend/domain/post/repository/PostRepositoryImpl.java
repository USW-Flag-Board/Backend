package com.FlagHome.backend.domain.post.repository;

import com.FlagHome.backend.domain.post.controller.dto.PostResponse;
import com.FlagHome.backend.domain.post.controller.dto.QPostResponse;
import com.FlagHome.backend.domain.post.entity.Post;
import com.FlagHome.backend.domain.post.entity.enums.PostStatus;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.FlagHome.backend.domain.member.entity.QMember.member;
import static com.FlagHome.backend.domain.post.entity.QPost.post;


@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    /**
     * Version 1
     */
    /* @Override
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
    } */

    /**
     * Version 2
     */
    @Override
    public Page<PostResponse> getAllPostsByBoard(String boardName, Pageable pageable) {
        List<PostResponse> result = queryFactory
                .select(new QPostResponse(
                        post.id,
                        post.title,
                        member.avatar.nickname,
                        post.createdAt,
                        post.viewCount,
                        post.replyList.size(),
                        post.likeCount,
                        post.isEdited))
                .from(post)
                .innerJoin(post.member, member)
                .where(post.board.name.eq(boardName),
                        post.status.in(PostStatus.NORMAL, PostStatus.REPORTED))
                .orderBy(post.createdAt.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPQLQuery<Post> countQuery = queryFactory
                .selectFrom(post)
                .innerJoin(post.member, member)
                .where(post.board.name.eq(boardName),
                        post.status.in(PostStatus.NORMAL, PostStatus.REPORTED));

        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchCount);
    }

    @Override
    public List<PostResponse> getAllPostsByLoginId(String loginId) {
        return queryFactory
                .select(new QPostResponse(
                        post.id,
                        post.title,
                        member.avatar.nickname,
                        post.createdAt,
                        post.viewCount,
                        post.replyList.size(),
                        post.likeCount,
                        post.isEdited))
                .from(post)
                .innerJoin(post.member, member)
                .where(member.loginId.eq(loginId))
                .fetch();
    }

    @Override
    public List<PostResponse> getTopFiveByCondition(String condition) {
        return queryFactory
                .select(new QPostResponse(
                        post.id,
                        post.title,
                        member.avatar.nickname,
                        post.createdAt,
                        post.viewCount,
                        post.replyList.size(),
                        post.likeCount,
                        post.isEdited))
                .from(post)
                .innerJoin(post.member, member)
                .orderBy(isLikeOrder(condition))
                .where(isLikeAndLatest(condition),
                        isLikeAndHotPost(condition))
                .limit(5)
                .fetch();
    }

    private OrderSpecifier<?> isLikeOrder(String condition) {
        return StringUtils.equals(condition, "like") ? post.likeCount.desc() : post.createdAt.asc();
    }

    private BooleanExpression isLikeAndLatest(String condition) {
        return StringUtils.equals(condition, "like") ? post.createdAt.after(LocalDateTime.now().minusWeeks(2)) : null;
    }

    private BooleanExpression isLikeAndHotPost(String condition) {
        return StringUtils.equals(condition, "like") ? post.likeCount.goe(5) : null;
    }
}
