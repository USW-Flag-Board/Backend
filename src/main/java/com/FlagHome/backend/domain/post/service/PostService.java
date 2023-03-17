package com.FlagHome.backend.domain.post.service;

import com.FlagHome.backend.domain.board.entity.Board;
import com.FlagHome.backend.domain.board.repository.BoardRepository;
import com.FlagHome.backend.global.common.Status;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.domain.post.dto.CreatePostRequest;
import com.FlagHome.backend.domain.post.dto.GetPostResponse;
import com.FlagHome.backend.domain.post.dto.LightPostDto;
import com.FlagHome.backend.domain.post.entity.Post;
import com.FlagHome.backend.domain.post.repository.PostRepository;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.FlagHome.backend.global.utility.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public long createPost(CreatePostRequest postDto) {
        Member memberEntity = memberRepository.findById(postDto.getUserId()).orElse(null);
        if(memberEntity == null)
            throw new CustomException(ErrorCode.USER_NOT_FOUND);

        if(!memberEntity.getId().equals(SecurityUtils.getMemberId()))
            throw new CustomException(ErrorCode.HAVE_NO_AUTHORITY);

        Board boardEntity = boardRepository.findById(postDto.getBoardId()).orElse(null);
        if(boardEntity == null)
            throw new CustomException(ErrorCode.BOARD_NOT_EXISTS);

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

    @Transactional
    public CreatePostRequest updatePost(CreatePostRequest postDto) {
        Post postEntity = postRepository.findById(postDto.getId()).orElse(null);
        if(postEntity == null)
            throw new CustomException(ErrorCode.POST_NOT_FOUND);

        if(!postEntity.getMember().getId().equals(SecurityUtils.getMemberId()))
            throw new CustomException(ErrorCode.HAVE_NO_AUTHORITY);

        Board boardEntity = boardRepository.findById(postDto.getBoardId()).orElse(null);
        if(boardEntity == null)
            throw new CustomException(ErrorCode.BOARD_NOT_EXISTS);

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
    }
}
