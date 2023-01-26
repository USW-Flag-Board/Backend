package com.FlagHome.backend.domain.like.service;

import com.FlagHome.backend.domain.like.entity.Like;
import com.FlagHome.backend.domain.like.enums.LikeType;
import com.FlagHome.backend.domain.like.repository.LikeRepository;
import com.FlagHome.backend.domain.post.entity.Post;
import com.FlagHome.backend.domain.post.repository.PostRepository;
import com.FlagHome.backend.domain.reply.entity.Reply;
import com.FlagHome.backend.domain.reply.repository.ReplyRepository;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final ReplyRepository replyRepository;

    @Transactional
    public void likeOrUnlike(long userId, long targetId, String targetType, boolean isLike) {
        List<Like> likeList = likeRepository.findLikeByUserId(userId);
        Like alreadyExistedLike = null;
        for(Like eachLike : likeList) {
            if(eachLike.getTargetId() == targetId && eachLike.getTargetType().equals(targetType)) {
                alreadyExistedLike = eachLike;
                break;
            }
        }

        if(isLike && alreadyExistedLike != null)
            throw new CustomException(ErrorCode.ALREADY_EXISTS_LIKE);
        else if(!isLike && alreadyExistedLike == null)
            throw new CustomException(ErrorCode.NOT_EXISTS_LIKE);

        int adder = (isLike) ? 1 : -1;
        LikeType likeType = LikeType.of(targetType);
        switch (likeType) {
            case POST -> {
                Post post = postRepository.findById(targetId).orElse(null);
                if(post == null)
                    throw new CustomException(ErrorCode.POST_NOT_FOUND);
                post.setLikeCount(post.getLikeCount() + adder);
            }
            case REPLY -> {
                Reply reply = replyRepository.findById(targetId).orElse(null);
                if(reply == null)
                    throw new CustomException(ErrorCode.REPLY_NOT_FOUND);

                // reply.setLikeCount(reply.getLikeCount() + adder);
                // 위의 코드는 Reply에 likeCount 대응하고 넣으면 됩니다.  (2023.01.26 윤희승)
            }
        }

        if(isLike)
            likeRepository.save(Like.builder().userId(userId).targetId(targetId).targetType(LikeType.of(targetType)).build());
        else
            likeRepository.delete(alreadyExistedLike);
    }
}
