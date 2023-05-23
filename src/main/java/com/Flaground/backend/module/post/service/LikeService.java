package com.Flaground.backend.module.post.service;

import com.Flaground.backend.global.exception.CustomException;
import com.Flaground.backend.global.exception.ErrorCode;
import com.Flaground.backend.module.post.domain.Like;
import com.Flaground.backend.module.post.domain.Likeable;
import com.Flaground.backend.module.post.domain.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;

    public void like(Long memberId, Likeable likeable) {
        isLiked(memberId, likeable);
        likeRepository.save(Like.from(memberId, likeable));
    }

    public void dislike(Long memberId, Likeable likeable) {
        Like like = findByIds(memberId, likeable);
        likeRepository.delete(like);
    }

    // todo : 게시글, 댓글이 삭제될 때 어떻게 처리할지?

    private void isLiked(Long memberId, Likeable likeable) {
        if (isExist(memberId, likeable)) {
            throw new CustomException(ErrorCode.ALREADY_LIKED);
        }
    }

    private boolean isExist(Long memberId, Likeable likeable) {
        return likeRepository.existsByIds(memberId, likeable);
    }

    private Like findByIds(Long memberId, Likeable likeable) {
        return likeRepository.findByIds(memberId, likeable)
                .orElseThrow(() -> new CustomException(ErrorCode.NEVER_LIKED));
    }
}
