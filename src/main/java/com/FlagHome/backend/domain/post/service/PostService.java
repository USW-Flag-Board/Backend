package com.FlagHome.backend.domain.post.service;

import com.FlagHome.backend.domain.board.entity.Board;
import com.FlagHome.backend.domain.board.service.BoardService;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.service.MemberService;
import com.FlagHome.backend.domain.post.controller.dto.response.PostDetailResponse;
import com.FlagHome.backend.domain.post.controller.dto.response.PostResponse;
import com.FlagHome.backend.domain.post.controller.dto.response.ReplyResponse;
import com.FlagHome.backend.domain.post.controller.dto.response.SearchResponse;
import com.FlagHome.backend.domain.post.entity.Post;
import com.FlagHome.backend.domain.post.entity.enums.SearchOption;
import com.FlagHome.backend.domain.post.entity.enums.SearchPeriod;
import com.FlagHome.backend.domain.post.entity.enums.TopPostCondition;
import com.FlagHome.backend.domain.post.like.service.PostLikeService;
import com.FlagHome.backend.domain.post.reply.service.ReplyService;
import com.FlagHome.backend.domain.post.repository.PostRepository;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberService memberService;
    private final BoardService boardService;
    private final ReplyService replyService;
    private final PostLikeService postLikeService;

    /**
     * Version 1
     */
    /* @Transactional
    public long createPost(CreatePostRequest postDto) {
        Member memberEntity = memberRepository.findById(postDto.getUserId()).orElse(null);
        if(memberEntity == null)
            throw new CustomException(ErrorCode.USER_NOT_FOUND);

        if(!memberEntity.getId().equals(SecurityUtils.getMemberId()))
            throw new CustomException(ErrorCode.HAVE_NO_AUTHORITY);

        Board boardEntity = boardRepository.findById(postDto.getBoardId()).orElse(null);
        if(boardEntity == null)
            throw new CustomException(ErrorCode.BOARD_NOT_FOUND);

        Post post = postRepository.save(Post.builder()
                .member(memberEntity)
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .imgUrl(postDto.getImgUrl())
                .fileUrl(postDto.getFileUrl())
                .board(boardEntity)
                .status(Status.NORMAL)
                .replyList(new ArrayList<>())
                .likeList(new ArrayList<>())
                .viewCount(0L)
                .build());

        return post.getId();
    }
    @Transactional
    public GetPostResponse getPost(long postId) {
        Post postEntity = postRepository.findById(postId).orElse(null);
        if(postEntity == null)
            throw new CustomException(ErrorCode.POST_NOT_FOUND);

        return new GetPostResponse(postEntity);
    }

    @Transactional(readOnly = true)
    public List<LightPostDto> getMemberPostByLoginId(String loginId) {
        return postRepository.findMyPostList(loginId);
    }

    @Transactional
    public CreatePostRequest updatePost(CreatePostRequest postDto) {
        Post postEntity = postRepository.findById(postDto.getId()).orElse(null);
        if(postEntity == null)
            throw new CustomException(ErrorCode.POST_NOT_FOUND);

        if(!postEntity.getMember().getId().equals(SecurityUtils.getMemberId()))
            throw new CustomException(ErrorCode.HAVE_NO_AUTHORITY);

        Board boardEntity = boardRepository.findById(postDto.getBoardId()).orElse(null);
        if(boardEntity == null)
            throw new CustomException(ErrorCode.BOARD_NOT_FOUND);

        postEntity.setTitle(postDto.getTitle());
        postEntity.setContent(postDto.getContent());
        postEntity.setBoard(boardEntity);

        return new CreatePostRequest(postEntity);
    }

    @Transactional
    public void deletePost(long postId) {
        Post postEntity = postRepository.findById(postId).orElse(null);
        if(postEntity == null)
            throw new CustomException(ErrorCode.POST_NOT_FOUND);

        if(!postEntity.getMember().getId().equals(SecurityUtils.getMemberId()))
            throw new CustomException(ErrorCode.HAVE_NO_AUTHORITY);

        postRepository.deleteById(postId);
    }

    @Transactional
    public List<LightPostDto> getTopNPostListByDateAndLike(int postCount) {
        return postRepository.findTopNPostListByDateAndLike(postCount);
    } */

    public PostDetailResponse getPost(Long postId) {
        Post post = findById(postId);
        post.isAccessible();
        post.increaseViewCount();
        return PostDetailResponse.from(post);
    }

    @Transactional(readOnly = true)
    public List<ReplyResponse> getAllReplies(Long postId) {
        return replyService.getAllReplies(postId);
    }

    @Transactional(readOnly = true)
    public ReplyResponse getBestReply(Long postId) {
        return replyService.getBestReply(postId);
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getAllPostsByBoard(String boardName, Pageable pageable) {
        boardService.findByName(boardName);
        return postRepository.getAllPostsByBoard(boardName, pageable);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getMemberPagePosts(String loginId) {
        Member member = memberService.findByLoginId(loginId);
        member.isAvailable();
        return postRepository.getAllPostsByLoginId(loginId);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getTopFivePostByCondition(TopPostCondition condition) {
        return postRepository.getTopFiveByCondition(condition);
    }

    @Transactional(readOnly = true)
    public SearchResponse searchPostsWithCondition(String boardName, String keyword,
                                                   SearchPeriod period, SearchOption option) {
        return postRepository.searchWithCondition(boardName, keyword, period, option);
    }

    public Long createPost(Long memberId, Post post, String boardName) {
        Member member = memberService.findById(memberId);
        Board board = boardService.findByName(boardName);
        return postRepository.save(Post.of(member, board, post)).getId();
    }

    public void commentReply(Long memberId, Long postId, String content) {
        Member member = memberService.findById(memberId);
        Post post = findById(postId);
        replyService.commentReply(member, post, content);
        post.increaseReplyCount();
    }

    public void updatePost(Long memberId, Long postId, Post newPost) {
        Post post = validateAuthorAndReturnPost(memberId, postId);
        post.updatePost(newPost);
    }

    public void updateReply(Long memberId, Long replyId, String content) {
        replyService.updateReply(memberId, replyId, content);
    }

    public void deletePost(Long memberId, Long postId) {
        Post post = validateAuthorAndReturnPost(memberId, postId);
        post.delete();
    }

    public void deleteReply(Long memberId, Long replyId) {
        Long postId = replyService.deleteReply(memberId, replyId);
        Post post = findById(postId);
        post.decreaseReplyCount();
    }

    public void likePost(Long memberId, Long postId) {
        Member member = memberService.findById(memberId);
        Post post = findById(postId);
        postLikeService.like(member, post);
    }

    public void likeReply(Long memberId, Long replyId) {
        Member member = memberService.findById(memberId);
        replyService.likeReply(member, replyId);
    }

    public void cancelLikePost(Long memberId, Long postId) {
        Post post = findById(postId);
        postLikeService.cancelLike(memberId, post);
    }

    public void cancelLikeReply(Long memberId, Long replyId) {
        replyService.cancelLikeReply(memberId, replyId);
    }

    private Post findById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

    private Post validateAuthorAndReturnPost(Long memberId, Long postId) {
        Post post = findById(postId);
        validateAuthor(memberId, post.getMember().getId());
        return post;
    }

    private void validateAuthor(Long memberId, Long targetId) {
        if (!Objects.equals(memberId, targetId)) {
            throw new CustomException(ErrorCode.NOT_AUTHOR);
        }
    }
}
