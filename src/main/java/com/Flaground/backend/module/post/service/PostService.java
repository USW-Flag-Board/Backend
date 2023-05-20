package com.Flaground.backend.module.post.service;

import com.Flaground.backend.global.exception.CustomException;
import com.Flaground.backend.global.exception.ErrorCode;
import com.Flaground.backend.module.board.service.BoardService;
import com.Flaground.backend.module.member.domain.Member;
import com.Flaground.backend.module.member.service.MemberService;
import com.Flaground.backend.module.post.controller.dto.response.GetPostResponse;
import com.Flaground.backend.module.post.controller.dto.response.PostResponse;
import com.Flaground.backend.module.post.controller.dto.response.SearchResponse;
import com.Flaground.backend.module.post.domain.Post;
import com.Flaground.backend.module.post.domain.PostData;
import com.Flaground.backend.module.post.domain.Reply;
import com.Flaground.backend.module.post.domain.enums.SearchOption;
import com.Flaground.backend.module.post.domain.enums.SearchPeriod;
import com.Flaground.backend.module.post.domain.enums.TopPostCondition;
import com.Flaground.backend.module.post.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberService memberService;
    private final BoardService boardService;
    private final ReplyService replyService;
    private final LikeService likeService;

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

    /**
     * Version 2
     */
    @Transactional(readOnly = true)
    public Page<PostResponse> getPostsOfBoard(String boardName, Pageable pageable) {
        boardService.isCorrectName(boardName);
        return postRepository.getPostsOfBoard(boardName, pageable);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getMemberPagePosts(String loginId) {
        Member member = memberService.findByLoginId(loginId);
        member.isAvailable();
        return postRepository.getPostsByLoginId(loginId);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getTopFivePostByCondition(TopPostCondition condition) {
        return postRepository.getTopFiveByCondition(condition);
    }

    @Transactional(readOnly = true)
    public SearchResponse searchPostsWithCondition(String boardName, String keyword,
                                                   SearchPeriod period, SearchOption option) {
        boardService.isCorrectName(boardName);
        return postRepository.searchWithCondition(boardName, keyword, period, option);
    }

    @Transactional(readOnly = true)
    public SearchResponse integrationSearch(String keyword) {
        return postRepository.integrationSearch(keyword);
    }

    public GetPostResponse get(Long memberId, Long postId) {
        Post post = findById(postId);
        post.isAccessible();
        return postRepository.getWithReplies(memberId, postId);
    }

    public Long create(Long memberId, PostData postData) {
        boardService.isCorrectName(postData.getBoardName());
        Member member = memberService.findById(memberId);
        return postRepository.save(Post.of(member, postData)).getId();
    }

    public void commentReply(Long memberId, Long postId, String content) {
        Member member = memberService.findById(memberId);
        Post post = findById(postId);
        post.addReply(Reply.of(member, postId, content));
    }

    public void update(Long memberId, Long postId, PostData postData) {
        boardService.isCorrectName(postData.getBoardName());
        Post post = validateAuthorAndReturnPost(memberId, postId);
        post.updatePost(postData);
    }

    public void updateReply(Long memberId, Long replyId, String content) {
        replyService.update(memberId, replyId, content);
    }

    public void delete(Long memberId, Long postId) {
        Post post = validateAuthorAndReturnPost(memberId, postId);
        post.delete();
    }

    public void deleteReply(Long memberId, Long postId, Long replyId) {
        Post post = findById(postId);
        Reply reply = replyService.delete(memberId, replyId);
        post.deleteReply(reply);
    }

    public int like(Long memberId, Long postId) {
        Post post = findById(postId);
        likeService.like(memberId, post);
        return post.like();
    }

    public int likeReply(Long memberId, Long replyId) {
        return replyService.like(memberId, replyId);
    }

    public int dislike(Long memberId, Long postId) {
        Post post = findById(postId);
        likeService.dislike(memberId, post);
        return post.dislike();
    }

    public int dislikeReply(Long memberId, Long replyId) {
        return replyService.dislike(memberId, replyId);
    }

    private Post findById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

    private Post validateAuthorAndReturnPost(Long memberId, Long postId) {
        Post post = findById(postId);
        post.validateAuthor(memberId);
        return post;
    }
}
