package com.FlagHome.backend.domain.reply.service;

import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.FlagHome.backend.domain.Status;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.post.entity.Post;
import com.FlagHome.backend.domain.post.repository.PostRepository;
import com.FlagHome.backend.domain.reply.dto.ReplyDto;
import com.FlagHome.backend.domain.reply.entity.Reply;
import com.FlagHome.backend.domain.reply.repository.ReplyRepository;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.global.utility.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final ReplyRepository replyRepository;

    @Transactional
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
                                .status(Status.ON)
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
    }
}