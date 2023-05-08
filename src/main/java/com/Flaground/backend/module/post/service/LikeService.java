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

    /**
     * Version 1
     */
    /*@Transactional
    public void like(Long memberId, Long targetId, LikeType likeType) {
        Member member = findMember(memberId);
        if(likeType == LikeType.POST)
            likeInner(findPost(targetId).getLikeList(), targetId, member, likeType);
        else if(likeType == LikeType.REPLY)
            likeInner(findReply(targetId).getLikeList(), targetId, member, likeType);
    }

    @Transactional
    public void unlike(Long memberId, Long targetId, LikeType likeType) {
        Member member = findMember(memberId);
        if(likeType == LikeType.POST)
            unlikeInner(findPost(targetId).getLikeList(), member);
        else if(likeType == LikeType.REPLY)
            unlikeInner(findReply(targetId).getLikeList(), member);
    }

    private void likeInner(List<Like> likeList, Long targetId, Member member, LikeType likeType) {
        for(Like eachLike : likeList) {
            if(eachLike.getMember() == member)
                throw new CustomException(ErrorCode.ALREADY_EXISTS_LIKE);
        }

        likeList.add(Like.builder()
                .member(member)
                .targetId(targetId)
                .targetType(likeType)
                .build());
    }

    private void unlikeInner(List<Like> likeList, Member member) {
        Like deleteTargetLike = null;
        for (Like like : likeList) {
            if (like.getMember() == member) {
                deleteTargetLike = like;
                break;
            }
        }

        if(deleteTargetLike != null) {
            likeList.remove(deleteTargetLike);
            likeRepository.delete(deleteTargetLike);
        }
    }

    private Post findPost(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if(post == null)
            throw new CustomException(ErrorCode.POST_NOT_FOUND);

        return post;
    }

    private Reply findReply(Long replyId) {
        Reply reply = replyRepository.findById(replyId).orElse(null);
        if(reply == null)
            throw new CustomException(ErrorCode.REPLY_NOT_FOUND);

        return reply;
    }

    private Member findMember(Long memberId) {
        Member member = memberRepository.findById(memberId).orElse(null);
        if(member == null)
            throw new CustomException(ErrorCode.USER_NOT_FOUND);

        return member;
    }*/

    /**
     * Version 2
     */
    public void like(Long memberId, Likeable likeable) {
        isLiked(memberId, likeable);
        likeRepository.save(Like.from(memberId, likeable));
    }

    public void dislike(Long memberId, Likeable likeable) {
        Like like = findByIds(memberId, likeable);
        likeRepository.delete(like);
    }

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
