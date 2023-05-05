package com.FlagHome.backend.module.post.like.service;

import com.FlagHome.backend.module.member.domain.Member;
import com.FlagHome.backend.module.post.like.Likeable;

public interface LikeService {
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
    boolean isExist(Long memberId, Long targetId);

    void like(Member member, Likeable likeable);

    void cancelLike(Long memberId, Likeable likeable);
}
