package com.FlagHome.backend.domain.post.like.service;

import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.post.like.Likeable;
import com.FlagHome.backend.domain.post.like.entity.Like;
import com.FlagHome.backend.domain.post.like.entity.ReplyLike;
import com.FlagHome.backend.domain.post.like.repository.LikeRepository;
import com.FlagHome.backend.domain.post.reply.entity.Reply;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyLikeService implements LikeService {
    private final LikeRepository likeRepository;

    @Override
    public boolean isExist(Long memberId, Long targetId) {
        return likeRepository.isReplyLiked(memberId, targetId);
    }

    @Override
    public void like(Member member, Likeable likeable) {
        Reply reply = (Reply) likeable;
        if (isExist(member.getId(), reply.getId())) {
            throw new CustomException(ErrorCode.ALREADY_LIKED);
        }
        likeRepository.save(ReplyLike.of(member, reply));
        reply.increaseLikeCount();
    }

    @Override
    public void cancelLike(Long memberId, Likeable likeable) {
        Reply reply = (Reply) likeable;
        Like like = findByMemberAndReply(memberId, reply.getId());
        likeRepository.delete(like);
        reply.decreaseLikeCount();
    }

    private Like findByMemberAndReply(Long memberId, Long replyId) {
        return likeRepository.findByMemberAndReply(memberId, replyId)
                .orElseThrow(() -> new CustomException(ErrorCode.NEVER_LIKED));
    }
}
