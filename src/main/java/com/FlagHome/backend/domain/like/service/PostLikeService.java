package com.FlagHome.backend.domain.like.service;

import com.FlagHome.backend.domain.like.repository.LikeRepository;
import com.FlagHome.backend.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostLikeService implements LikeService {
    private LikeRepository likeRepository;
    private PostService postService;

    @Override
    public void like(Long memberId, Long targetId) {

    }

    @Override
    public void cancelLike(Long memberId, Long targetId) {

    }
}
