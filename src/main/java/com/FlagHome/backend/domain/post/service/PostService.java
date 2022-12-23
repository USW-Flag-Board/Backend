package com.FlagHome.backend.domain.post.service;

import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.domain.post.dto.PostDto;
import com.FlagHome.backend.domain.post.entity.Post;
import com.FlagHome.backend.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class PostService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Transactional
    public void createPost(PostDto postDto) {
        Member memberEntity = memberRepository.findById(postDto.getUserId()).orElse(null);
        if(memberEntity == null)
            throw new CustomException(ErrorCode.USER_NOT_FOUND);

        postRepository.save(Post.builder()
                .member(memberEntity)
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .category(postDto.getCategory())
                .status(postDto.getStatus())
                .replyList(new ArrayList<>())
                .viewCount(0L)
                .build());
    }

    @Transactional
    public PostDto getPost(long postId) {
        Post postEntity = postRepository.findById(postId).orElse(null);
        if(postEntity == null)
            throw new CustomException(ErrorCode.POST_NOT_FOUND);

        return new PostDto(postEntity);
    }

    @Transactional
    public void updatePost(PostDto postDto) {
        Post postEntity = postRepository.findById(postDto.getId()).orElse(null);
        if(postEntity == null)
            throw new CustomException(ErrorCode.POST_NOT_FOUND);

        postEntity.setTitle(postDto.getTitle());
        postEntity.setContent(postDto.getContent());
        postEntity.setCategory(postDto.getCategory());
    }

    @Transactional
    public void deletePost(long postId) {
        postRepository.deleteById(postId);
    }
}