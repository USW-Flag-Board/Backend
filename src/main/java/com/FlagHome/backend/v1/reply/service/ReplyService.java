package com.FlagHome.backend.v1.reply.service;

import com.FlagHome.backend.v1.Status;
import com.FlagHome.backend.v1.post.entity.Post;
import com.FlagHome.backend.v1.post.repository.PostRepository;
import com.FlagHome.backend.v1.reply.dto.ReplyDto;
import com.FlagHome.backend.v1.reply.entity.Reply;
import com.FlagHome.backend.v1.reply.repository.ReplyRepository;
import com.FlagHome.backend.v1.user.entity.User;
import com.FlagHome.backend.v1.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
public class ReplyService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReplyRepository replyRepository;

    public ReplyDto createReply(ReplyDto replyDto) {
        Post foundPost = postRepository.findById(replyDto.getPostId()).orElse(null);
        User foundUser = userRepository.findById(replyDto.getUserId()).orElse(null);

        Reply replyEntity = Reply.builder()
                                .user(foundUser)
                                .post(foundPost)
                                .content(replyDto.getContent())
                                .replyGroup(replyDto.getReplyGroup())
                                .replyOrder(replyDto.getReplyOrder())
                                .replyDepth(replyDto.getReplyDepth())
                                .status(Status.ON)
                                .build();
        replyRepository.save(replyEntity);

        replyDto.setId(replyEntity.getId());
        return replyDto;
    }

    public List<ReplyDto> findReplies(Long postId) {
        List<Reply> foundReplyEntities = replyRepository.findByPostId(postId);
        List<ReplyDto> replyDtoList = new LinkedList<>();
        for(Reply replyEntity : foundReplyEntities)
            replyDtoList.add(new ReplyDto(replyEntity));

        return replyDtoList;
    }

    public void deleteReply(long replyId) {
        Reply replyEntity = replyRepository.findById(replyId).orElse(null);
        if(replyEntity != null) {
            long replyEntityGroup = replyEntity.getReplyGroup();
            long postId = replyEntity.getPost().getId();

            if(replyEntity.getReplyDepth() == 0) {
                List<Reply> allReplies = replyRepository.findByPostId(replyEntity.getPost().getId());
                System.out.println("올리플 : " + allReplies.size());
                List<Reply> deleteCandidate = new LinkedList<>();
                for(Reply eachReply : allReplies) {
                    long eachReplyGroup = eachReply.getReplyGroup();
                    if(replyEntityGroup < eachReplyGroup)
                        eachReply.setReplyGroup(eachReplyGroup - 1);
                    else if(replyEntityGroup == eachReplyGroup)
                        deleteCandidate.add(eachReply);
                }
                replyRepository.deleteAll(deleteCandidate);
            }
            else {
                List<Reply> sameGroupReplies = replyRepository.findByPostIdAndReplyGroup(postId, replyEntityGroup);
                long replyEntityOrder = replyEntity.getReplyOrder();
                for(Reply eachReply : sameGroupReplies) {
                    long eachReplyOrder = eachReply.getReplyOrder();
                    if(replyEntityOrder < eachReplyOrder)
                        eachReply.setReplyOrder(eachReplyOrder - 1);
                }
                replyRepository.delete(replyEntity);
            }
        }
    }

    public ReplyDto updateReply(ReplyDto replyDto) {
        Reply reply = replyRepository.findById(replyDto.getId()).orElse(null);
        assert reply != null;
        reply.setContent(replyDto.getContent());
        return replyDto;
    }
}