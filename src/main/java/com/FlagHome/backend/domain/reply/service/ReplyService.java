package com.FlagHome.backend.domain.reply.service;

import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.service.MemberService;
import com.FlagHome.backend.domain.post.entity.Post;
import com.FlagHome.backend.domain.post.service.PostService;
import com.FlagHome.backend.domain.reply.controller.dto.ReplyResponse;
import com.FlagHome.backend.domain.reply.entity.Reply;
import com.FlagHome.backend.domain.reply.repository.ReplyRepository;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final PostService postService;
    private final MemberService memberService;

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

    @Transactional
    public void create(Long memberId, Long postId, String content) {
        Member member = memberService.findById(memberId);
        Post post = postService.findById(postId);
        replyRepository.save(Reply.of(member, post, content));
    }

    @Transactional
    public void update(Long memberId, Long replyId, String content ) {
        Member member = memberService.findById(memberId);
        Reply reply = validateAuthorAndReturn(member, replyId);
        reply.renewContent(content);
    }

    @Transactional
    public void delete(Long memberId, Long replyId) {
        Member member = memberService.findById(memberId);
        Reply reply = validateAuthorAndReturn(member, replyId);
        replyRepository.delete(reply);
    }

    private Reply validateAuthorAndReturn(Member member, Long replyId) {
        Reply reply = findById(replyId);

        if (!StringUtils.equals(member.getEmail(), reply.getMember().getEmail())) {
            throw new CustomException(ErrorCode.NOT_AUTHOR);
        }

        return reply;
    }

    private Reply findById(Long replyId) {
        return replyRepository.findById(replyId)
                .orElseThrow(() -> new CustomException(ErrorCode.REPLY_NOT_FOUND));
    }
}