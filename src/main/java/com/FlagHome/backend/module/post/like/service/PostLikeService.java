package com.FlagHome.backend.module.post.like.service;

import com.FlagHome.backend.module.member.domain.Member;
import com.FlagHome.backend.module.post.entity.Post;
import com.FlagHome.backend.module.post.like.Likeable;
import com.FlagHome.backend.module.post.like.entity.Like;
import com.FlagHome.backend.module.post.like.entity.PostLike;
import com.FlagHome.backend.module.post.like.repository.LikeRepository;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostLikeService implements LikeService {
    private final LikeRepository likeRepository;

    @Override
    public boolean isExist(Long memberId, Long targetId) {
        return likeRepository.isPostLiked(memberId, targetId);
    }

    @Override
    public void like(Member member, Likeable likeable) {
        Post post = (Post) likeable;
        if (isExist(member.getId(), post.getId())) {
            throw new CustomException(ErrorCode.ALREADY_LIKED);
        }
        likeRepository.save(PostLike.of(member, post));
        post.increaseLikeCount();
    }

    @Override
    public void cancelLike(Long memberId, Likeable likeable) {
        Post post = (Post) likeable;
        Like like = findByMemberAndPost(memberId, post.getId());
        likeRepository.delete(like);
        post.decreaseLikeCount();
    }

    private Like findByMemberAndPost(Long memberId, Long postId) {
        return likeRepository.findByMemberAndPost(memberId, postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NEVER_LIKED));
    }
}
