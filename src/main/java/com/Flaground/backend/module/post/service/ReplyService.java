package com.Flaground.backend.module.post.service;

import com.Flaground.backend.global.exception.CustomException;
import com.Flaground.backend.global.exception.ErrorCode;
import com.Flaground.backend.module.post.domain.Reply;
import com.Flaground.backend.module.post.domain.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final LikeService likeService;

    public void update(Long memberId, Long replyId, String content) {
        Reply reply = validateAuthorAndReturnReply(memberId, replyId);
        reply.updateContent(content);
    }

    public Reply delete(Long memberId, Long replyId) {
        Reply reply = validateAuthorAndReturnReply(memberId, replyId);
        replyRepository.delete(reply);
        return reply;
    }

    public int like(Long memberId, Long replyId) {
        Reply reply = findById(replyId);
        likeService.like(memberId, reply);
        return reply.like();
    }

    public int dislike(Long memberId, Long replyId) {
        Reply reply = findById(replyId);
        likeService.dislike(memberId, reply);
        return reply.dislike();
    }

    private Reply validateAuthorAndReturnReply(Long memberId, Long replyId) {
        Reply reply = findById(replyId);
        reply.validateAuthor(memberId);
        return reply;
    }

    public Reply findById(Long replyId) {
        return replyRepository.findById(replyId)
                .orElseThrow(() -> new CustomException(ErrorCode.REPLY_NOT_FOUND));
    }
}