package com.FlagHome.backend.domain.like.service;

import com.FlagHome.backend.domain.like.repository.LikeRepository;
import com.FlagHome.backend.domain.reply.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReplyLikeService implements LikeService {
    private LikeRepository likeRepository;
    private ReplyService replyService;

    @Override
    public void like(Long memberId, Long targetId) {

    }

    @Override
    public void cancelLike(Long memberId, Long targetId) {

    }
}
