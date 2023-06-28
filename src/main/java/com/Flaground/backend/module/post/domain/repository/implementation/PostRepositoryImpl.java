package com.Flaground.backend.module.post.domain.repository.implementation;

import com.Flaground.backend.global.common.response.SearchResponse;
import com.Flaground.backend.module.post.controller.dto.response.GetPostResponse;
import com.Flaground.backend.module.post.controller.dto.response.PostDetailResponse;
import com.Flaground.backend.module.post.controller.dto.response.PostResponse;
import com.Flaground.backend.module.post.controller.dto.response.QLikeResponse;
import com.Flaground.backend.module.post.controller.dto.response.QPostDetailResponse;
import com.Flaground.backend.module.post.controller.dto.response.QPostResponse;
import com.Flaground.backend.module.post.controller.dto.response.QReplyResponse;
import com.Flaground.backend.module.post.controller.dto.response.ReplyResponse;
import com.Flaground.backend.module.post.domain.Post;
import com.Flaground.backend.module.post.domain.enums.LikeType;
import com.Flaground.backend.module.post.domain.enums.PostStatus;
import com.Flaground.backend.module.post.domain.enums.ReplyStatus;
import com.Flaground.backend.module.post.domain.enums.SearchOption;
import com.Flaground.backend.module.post.domain.enums.SearchPeriod;
import com.Flaground.backend.module.post.domain.enums.TopPostCondition;
import com.Flaground.backend.module.post.domain.repository.ImageRepository;
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

import static com.Flaground.backend.module.board.domain.BoardType.NOTICE;
import static com.Flaground.backend.module.member.domain.QMember.member;
import static com.Flaground.backend.module.post.domain.QLike.like;
import static com.Flaground.backend.module.post.domain.QPost.post;
import static com.Flaground.backend.module.post.domain.QReply.reply;
import static com.querydsl.core.types.dsl.Expressions.asNumber;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final ImageRepository imageRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public GetPostResponse getWithReplies(Long memberId, Long postId) {
        PostDetailResponse postDetailResponse = fetchPostResponse(memberId, postId);
        List<String> keys = imageRepository.getKeysByPostId(postId);
        List<ReplyResponse> replyResponses = fetchReplyResponses(memberId, postId);
        return new GetPostResponse(postDetailResponse, keys, replyResponses);
    }

    @Override
    public Page<PostResponse> getPostsOfBoard(String boardName, Pageable pageable) {
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
                        isAccessiblePost())
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPQLQuery<Post> countQuery = queryFactory
                .selectFrom(post)
                .where(post.boardName.eq(boardName),
                        isAccessiblePost());

        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchCount);
    }

    @Override
    public List<PostResponse> getPostsByLoginId(String loginId) {
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
                .where(member.loginId.eq(loginId),
                        isAccessiblePost())
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
                .where(isLatestPosts(condition),
                        isHotPosts(condition),
                        isAccessiblePost())
                .limit(5)
                .fetch();
    }

    @Override
    public List<PostResponse> getNotice() {
        return queryFactory
                .select(new QPostResponse(
                        post.id,
                        post.title,
                        Expressions.constant("관리자"),
                        post.createdAt,
                        post.viewCount,
                        post.replies.size(),
                        post.likeCount,
                        post.isEdited))
                .from(post)
                .orderBy(post.createdAt.desc())
                .where(post.boardName.eq(NOTICE.getBoardName()),
                        isAccessiblePost())
                .limit(5)
                .fetch();
    }

    @Override
    public SearchResponse<PostResponse> integrationSearch(String keyword) {
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
                .where(post.content.contains(keyword)
                        .or(post.title.contains(keyword)),
                        isAccessiblePost())
                .orderBy(post.createdAt.desc())
                .fetch();

        return new SearchResponse<>(result);
    }

    @Override
    public SearchResponse<PostResponse> searchWithCondition(String boardName, String keyword, SearchPeriod period, SearchOption option) {
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
                .innerJoin(post.member, member);

        if (option.containsReply()) {
            query.innerJoin(reply).on(reply.postId.eq(post.id));
        }

        query.where(post.boardName.eq(boardName),
                        period.toExpression(),
                        option.toExpression(keyword),
                        isAccessiblePost())
                .orderBy(post.createdAt.desc());

        return new SearchResponse<>(query.fetch());
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

    // todo : 성능 이슈 해결하기 (select 1번 => n(댓글 수)개, select 2n번, 총 2n+1회..)
    private List<ReplyResponse> fetchReplyResponses(Long memberId, Long postId) {
        List<Long> replyIds = fetchReplyIds(postId);
        return replyIds.stream()
                .map(replyId -> fetchReplyResponse(memberId, replyId))
                .toList();
    }

    private List<Long> fetchReplyIds(Long postId) {
        return queryFactory
                .select(reply.id)
                .from(reply)
                .where(reply.postId.eq(postId),
                        isAccessibleReply())
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
                .where(reply.id.eq(replyId)) // id를 가져오는 작업에서 접근가능한 댓글인지 확인.
                .fetchOne();
    }

    private static BooleanExpression isAccessiblePost() {
        return post.status.in(PostStatus.NORMAL, PostStatus.REPORTED);
    }

    private static BooleanExpression isAccessibleReply() {
        return reply.status.ne(ReplyStatus.BANNED);
    }

    private BooleanExpression isLatestPosts(TopPostCondition condition) {
        final LocalDateTime limit = condition.isLikeCondition() ? LocalDateTime.now().minusWeeks(2) : LocalDateTime.now().minusWeeks(1);
        return post.createdAt.after(limit);
    }

    private BooleanExpression isHotPosts(TopPostCondition condition) {
        return condition.isLikeCondition() ? post.likeCount.goe(1) : null;
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
