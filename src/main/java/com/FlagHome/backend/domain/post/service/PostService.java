package com.FlagHome.backend.domain.post.service;

import com.FlagHome.backend.domain.board.entity.Board;
import com.FlagHome.backend.domain.board.service.BoardService;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.service.MemberService;
import com.FlagHome.backend.domain.post.entity.Post;
import com.FlagHome.backend.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberService memberService;
    private final BoardService boardService;

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

    // 예외 생각하기
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

    @Transactional(readOnly = true)
    public void getAllPosts(String boardName) {

    }

    public void getPost(Long postId) {

    }

    public Post create(Long memberId, Post post, String boardName) {
        Member member = memberService.findById(memberId);
        Board board = boardService.findByName(boardName);
        return postRepository.save(Post.of(member, post, board));
    }

    public Post update(Long memberId, Post post) {
        return null;
    }

    public void delete(Long memberId, Long postId) {

    }
}
