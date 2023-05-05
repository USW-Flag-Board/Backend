package com.FlagHome.backend.module.post.repository;

import com.FlagHome.backend.module.member.domain.Member;
import com.FlagHome.backend.module.post.controller.dto.response.PostResponse;
import com.FlagHome.backend.module.post.controller.dto.response.QPostResponse;
import com.FlagHome.backend.module.post.controller.dto.response.SearchResponse;
import com.FlagHome.backend.module.post.entity.Post;
import com.FlagHome.backend.module.post.entity.enums.PostStatus;
import com.FlagHome.backend.module.post.entity.enums.SearchOption;
import com.FlagHome.backend.module.post.entity.enums.SearchPeriod;
import com.FlagHome.backend.module.post.entity.enums.TopPostCondition;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.FlagHome.backend.module.board.entity.QBoard.board;
import static com.FlagHome.backend.module.member.domain.QMember.member;
import static com.FlagHome.backend.module.post.entity.QPost.post;
import static com.FlagHome.backend.module.post.reply.entity.QReply.reply;

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
    // todo: Pageable 말고 다른 객체로 받기
    @Override
    public Page<PostResponse> getAllPostsByBoard(String boardName, Pageable pageable) {
        List<PostResponse> result = queryFactory
                .select(new QPostResponse(
                        post.id,
                        post.title,
                        member.avatar.nickname,
                        post.createdAt,
                        post.viewCount,
                        post.replyCount,
                        post.likeCount,
                        post.isEdited))
                .from(post)
                .innerJoin(post.member, member)
                .where(post.board.name.eq(boardName),
                        post.status.in(PostStatus.NORMAL, PostStatus.REPORTED))
                .orderBy(post.createdAt.desc())
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
                        post.replyCount,
                        post.likeCount,
                        post.isEdited))
                .from(post)
                .innerJoin(post.member, member)
                .where(member.loginId.eq(loginId))
                .orderBy(post.createdAt.desc())
                .fetch();
    }

    @Override
    public List<PostResponse> getTopFiveByCondition(TopPostCondition condition) {
        return queryFactory
                .select(new QPostResponse(
                        post.id,
                        post.title,
                        member.avatar.nickname,
                        post.createdAt,
                        post.viewCount,
                        post.replyCount,
                        post.likeCount,
                        post.isEdited))
                .from(post)
                .innerJoin(post.member, member)
                .orderBy(condition.getOrder())
                .where(isLikeAndLatest(condition),
                        isLikeAndHotPost(condition))
                .limit(5)
                .fetch();
    }

    @Override
    public SearchResponse integrationSearch(String keyword) {
        List<PostResponse> result = queryFactory
                .selectDistinct(new QPostResponse(
                        post.id,
                        post.title,
                        member.avatar.nickname,
                        post.createdAt,
                        post.viewCount,
                        post.replyCount,
                        post.likeCount,
                        post.isEdited))
                .from(post)
                .innerJoin(post.member, member)
                .where(post.content.contains(keyword)
                        .or(post.title.contains(keyword)))
                .orderBy(post.createdAt.desc())
                .fetch();

        return SearchResponse.from(result);
    }

    @Override
    public SearchResponse searchWithCondition(String boardName, String keyword, SearchPeriod period, SearchOption option) {
        JPAQuery<PostResponse> query = queryFactory
                .selectDistinct(new QPostResponse(
                        post.id,
                        post.title,
                        member.avatar.nickname,
                        post.createdAt,
                        post.viewCount,
                        post.replyCount,
                        post.likeCount,
                        post.isEdited))
                .from(post)
                .innerJoin(post.member, member)
                .innerJoin(post.board, board)
                .where(board.name.eq(boardName),
                        period.getExpression(),
                        option.getExpression(keyword))
                .orderBy(post.createdAt.desc());

        if (option.isContainsReply()) {
            query.leftJoin(reply).on(reply.content.contains(keyword)).distinct();
        }

        return SearchResponse.from(query.fetch());
    }

    private BooleanExpression isLikeAndLatest(TopPostCondition condition) {
        return condition == TopPostCondition.like ? post.createdAt.after(LocalDateTime.now().minusWeeks(2)) : null;
    }

    private BooleanExpression isLikeAndHotPost(TopPostCondition condition) {
        return condition == TopPostCondition.like ? post.likeCount.goe(5) : null;
    }
}