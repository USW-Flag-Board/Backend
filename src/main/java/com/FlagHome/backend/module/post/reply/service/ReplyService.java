package com.FlagHome.backend.module.post.reply.service;

import com.FlagHome.backend.module.member.domain.Member;
import com.FlagHome.backend.module.post.entity.Post;
import com.FlagHome.backend.module.post.controller.dto.response.ReplyResponse;
import com.FlagHome.backend.module.post.like.service.ReplyLikeService;
import com.FlagHome.backend.module.post.reply.entity.Reply;
import com.FlagHome.backend.module.post.reply.repository.ReplyRepository;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyService {
    private final ReplyRepository replyRepository;

    private final ReplyLikeService replyLikeService;

    /**
     * Version 1
     */
    /* @Transactional
    public ReplyDto createReply(ReplyDto replyDto) {
        Post foundPost = postRepository.findById(replyDto.getPostId()).orElse(null);
        if(foundPost == null)
            throw new CustomException(ErrorCode.POST_NOT_FOUND);

        if(!foundPost.getMember().getId().equals(SecurityUtils.getMemberId()))
            throw new CustomException(ErrorCode.HAVE_NO_AUTHORITY);

        Member foundMember = memberRepository.findById(replyDto.getMemberId()).orElse(null);
        if(foundMember == null)
            throw new CustomException(ErrorCode.USER_NOT_FOUND);

        Reply replyEntity = Reply.builder()
                                .member(foundMember)
                                .post(foundPost)
                                .content(replyDto.getContent())
                                .replyGroup(replyDto.getReplyGroup())
                                .replyOrder(replyDto.getReplyOrder())
                                .replyDepth(replyDto.getReplyDepth())
                                .likeList(new ArrayList<>())
                                .status(Status.NORMAL)
                                .build();

        List<Reply> replyList = foundPost.getReplyList();
        replyList.add(replyEntity);

        return replyDto;
    }

    @Transactional
    public List<ReplyDto> findReplies(Long postId) {
        Post foundPost = postRepository.findById(postId).orElse(null);
        if(foundPost == null)
            throw new CustomException(ErrorCode.POST_NOT_FOUND);

        List<Reply> foundReplyEntities = foundPost.getReplyList();
        List<ReplyDto> replyDtoList = new LinkedList<>();
        for(Reply replyEntity : foundReplyEntities)
            replyDtoList.add(new ReplyDto(replyEntity));

        return replyDtoList;
    }

    @Transactional
    public void deleteReply(long replyId) {
        Reply replyEntity = replyRepository.findById(replyId).orElse(null);
        if(replyEntity == null)
            throw new CustomException(ErrorCode.REPLY_NOT_FOUND);

        if(!replyEntity.getMember().getId().equals(SecurityUtils.getMemberId()))
            throw new CustomException(ErrorCode.HAVE_NO_AUTHORITY);

        Post postEntity = replyEntity.getPost();
        long replyEntityGroup = replyEntity.getReplyGroup();

        List<Reply> allReplies = postEntity.getReplyList();
        if(replyEntity.getReplyDepth() == 0) {
            for(int i = 0; i < allReplies.size(); ++i) {
                Reply eachReply = allReplies.get(i);
                long eachReplyGroup = eachReply.getReplyGroup();
                if(replyEntityGroup < eachReplyGroup)
                    eachReply.setReplyGroup(eachReplyGroup - 1);
                else if(replyEntityGroup == eachReplyGroup) {
                    eachReply.setPost(null);
                    allReplies.set(i, null);
                }
            }
        }
        else {
            long replyEntityOrder = replyEntity.getReplyOrder();
            int deleteTargetIndex = 0;
            for(int i = 0; i < allReplies.size(); ++i) {
                Reply eachReply = allReplies.get(i);
                if(replyEntityGroup == eachReply.getReplyGroup()) {
                    long eachReplyOrder = eachReply.getReplyOrder();
                    if(replyEntityOrder < eachReplyOrder)
                        eachReply.setReplyOrder(eachReplyOrder - 1);
                }

                if(eachReply == replyEntity) {
                    eachReply.setPost(null);
                    deleteTargetIndex = i;
                }
            }
            allReplies.set(deleteTargetIndex, null);
        }
    }

    @Transactional
    public ReplyDto updateReply(ReplyDto replyDto) {
        Post postEntity = postRepository.findById(replyDto.getPostId()).orElse(null);
        if(postEntity == null)
            throw new CustomException(ErrorCode.POST_NOT_FOUND);

        if(!postEntity.getMember().getId().equals(SecurityUtils.getMemberId()))
            throw new CustomException(ErrorCode.HAVE_NO_AUTHORITY);

        List<Reply> postReplyList = postEntity.getReplyList();
        for(Reply eachReply : postReplyList) {
            if(eachReply.getId() == replyDto.getId()) {
                eachReply.setContent(replyDto.getContent());
                break;
            }
        }

        return replyDto;
    } */

    /**
     * Version 2
     */
    @Transactional(readOnly = true)
    public List<ReplyResponse> getAllReplies(Long postId) {
        return replyRepository.getAllReplies(postId);
    }

    @Transactional(readOnly = true)
    public ReplyResponse getBestReply(Long postId) {
        return replyRepository.getBestReply(postId);
    }

    public void commentReply(Member member, Post post, String content) {
        replyRepository.save(Reply.of(member, post, content));
    }

    public void updateReply(Long memberId, Long replyId, String content ) {
        Reply reply = validateAuthorAndReturnReply(memberId, replyId);
        reply.renewContent(content);
    }

    public Long deleteReply(Long memberId, Long replyId) {
        Reply reply = validateAuthorAndReturnReply(memberId, replyId);
        replyRepository.delete(reply);
        return reply.getPost().getId();
    }

    public void likeReply(Member member, Long replyId) {
        Reply reply = findById(replyId);
        replyLikeService.like(member, reply);
    }

    public void cancelLikeReply(Long memberId, Long replyId) {
        Reply reply = findById(replyId);
        replyLikeService.cancelLike(memberId, reply);
    }

    private Reply findById(Long replyId) {
        return replyRepository.findById(replyId)
                .orElseThrow(() -> new CustomException(ErrorCode.REPLY_NOT_FOUND));
    }

    private Reply validateAuthorAndReturnReply(Long memberId, Long replyId) {
        Reply reply = findById(replyId);
        validateAuthor(memberId, reply.getMember().getId());
        return reply;
    }

    private void validateAuthor(Long memberId, Long targetId) {
        if (!Objects.equals(memberId, targetId)) {
            throw new CustomException(ErrorCode.NOT_AUTHOR);
        }
    }
}