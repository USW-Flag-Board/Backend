package com.FlagHome.backend.domain.post.like.service;

import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.post.like.Likeable;
import com.FlagHome.backend.domain.post.like.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyLikeService implements LikeService {
    private LikeRepository likeRepository;

    @Override
    public boolean isExist(Long memberId, Long targetId) {
        return false;
    }

    @Override
    public void like(Member member, Likeable likeable) {

    }

    @Override
    public void cancelLike(Long memberId, Likeable likeable) {

    }
}
