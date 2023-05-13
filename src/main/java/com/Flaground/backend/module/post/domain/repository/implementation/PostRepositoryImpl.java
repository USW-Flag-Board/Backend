package com.Flaground.backend.module.post.domain.repository.implementation;

import com.Flaground.backend.module.post.controller.dto.response.*;
import com.Flaground.backend.module.post.domain.Post;
import com.Flaground.backend.module.post.domain.enums.*;
import com.Flaground.backend.module.post.domain.repository.PostRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.Flaground.backend.module.member.domain.QMember.member;
import static com.Flaground.backend.module.post.domain.QLike.like;
import static com.Flaground.backend.module.post.domain.QPost.post;
import static com.Flaground.backend.module.post.domain.QReply.reply;
import static com.querydsl.core.types.dsl.Expressions.asNumber;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private static final int HOT_POST_LIMIT = 5;
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
    public GetPostResponse getWithReplies(Long memberId, Long postId) {
        PostDetailResponse postDetailResponse = fetchPostResponse(memberId, postId);
        List<ReplyResponse> replyResponses = fetchReplyResponses(memberId, postId);
        return GetPostResponse.of(postDetailResponse, replyResponses);
    }

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
                        post.replies.size(),
                        post.likeCount,
                        post.isEdited))
                .from(post)
                .innerJoin(post.member, member)
                .where(post.boardName.eq(boardName),
                        post.status.in(PostStatus.NORMAL, PostStatus.REPORTED))
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPQLQuery<Post> countQuery = queryFactory
                .selectFrom(post)
                .innerJoin(post.member, member)
                .where(post.boardName.eq(boardName),
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
                        post.replies.size(),
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
                        post.replies.size(),
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
                        post.replies.size(),
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
                        post.replies.size(),
                        post.likeCount,
                        post.isEdited))
                .from(post)
                .innerJoin(post.member, member)
                .where(post.boardName.eq(boardName),
                        period.getExpression(),
                        option.getExpression(keyword))
                .orderBy(post.createdAt.desc());

        if (option.isContainsReply()) {
            query.leftJoin(reply).on(reply.content.contains(keyword)).distinct();
        }

        return SearchResponse.from(query.fetch());
    }

    private PostDetailResponse fetchPostResponse(Long memberId, Long postId) {
        return queryFactory
                .select(new QPostDetailResponse(
                        asNumber(postId),
                        member.loginId,
                        member.avatar.nickname,
                        member.avatar.profileImage,
                        post.boardName,
                        post.title,
                        post.content,
                        post.createdAt,
                        post.viewCount,
                        new QLikeResponse(
                                isLoggedIn(memberId, postId, LikeType.POST),
                                post.likeCount),
                        post.isEdited))
                .from(post)
                .innerJoin(post.member, member)
                .where(post.id.eq(postId))
                .fetchOne();
    }

    // todo : 성능 이슈 해결하기
    private List<ReplyResponse> fetchReplyResponses(Long memberId, Long postId) {
        List<Long> replyIds = fetchReplyIds(postId);
        return replyIds.stream()
                .map(replyId -> fetchReplyResponse(memberId, replyId))
                .collect(Collectors.toList());
    }

    private List<Long> fetchReplyIds(Long postId) {
        return queryFactory
                .select(reply.id)
                .from(reply)
                .where(reply.postId.eq(postId))
                .fetch();
    }

    private ReplyResponse fetchReplyResponse(Long memberId, Long replyId) {
        return queryFactory
                .select(new QReplyResponse(
                        reply.id,
                        member.loginId,
                        member.avatar.nickname,
                        member.avatar.profileImage,
                        reply.content,
                        new QLikeResponse(
                                isLoggedIn(memberId, replyId, LikeType.REPLY),
                                reply.likeCount),
                        reply.createdAt,
                        reply.isEdited))
                .from(reply)
                .innerJoin(reply.member, member)
                .where(reply.id.eq(replyId))
                .fetchOne();
    }

    private BooleanExpression isLikeAndLatest(TopPostCondition condition) {
        return condition.isLikeCondition() ? post.createdAt.after(LocalDateTime.now().minusWeeks(2)) : null;
    }

    private BooleanExpression isLikeAndHotPost(TopPostCondition condition) {
        return condition.isLikeCondition() ? post.likeCount.goe(HOT_POST_LIMIT) : null;
    }

    private BooleanExpression isLoggedIn(Long memberId, Long likeableId, LikeType likeType) {
        return memberId == null ? Expressions.FALSE : isLiked(memberId, likeableId, likeType);
    }

    private BooleanExpression isLiked(Long memberId, Long likeableId, LikeType likeType) {
        return Expressions.asBoolean(queryFactory
                .selectFrom(like)
                .where(like.memberId.eq(memberId),
                        like.likeableId.eq(likeableId),
                        like.likeType.eq(likeType))
                .fetchFirst() != null);
    }
}
