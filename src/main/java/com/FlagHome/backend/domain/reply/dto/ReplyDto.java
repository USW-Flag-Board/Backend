package com.FlagHome.backend.domain.reply.dto;

import com.FlagHome.backend.domain.common.Status;
import com.FlagHome.backend.domain.like.entity.Like;
import com.FlagHome.backend.domain.like.entity.LikeDto;
import com.FlagHome.backend.domain.reply.entity.Reply;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class ReplyDto {
    private long id;
    private long memberId;
    private long postId;
    private String content;
    private List<LikeDto> likeList;
    private long replyGroup;
    private long replyOrder;
    private long replyDepth;
    private Status status;

    public ReplyDto(Reply replyEntity) {
        this.id = replyEntity.getId();
        this.memberId = replyEntity.getMember().getId();
        this.postId = replyEntity.getPost().getId();
        this.content = replyEntity.getContent();
        this.replyGroup = replyEntity.getReplyGroup();
        this.replyOrder = replyEntity.getReplyOrder();
        this.replyDepth = replyEntity.getReplyDepth();
        this.status = replyEntity.getStatus();

        this.likeList = new ArrayList<>();
        for(Like eachLike : replyEntity.getLikeList())
            this.likeList.add(new LikeDto(eachLike.getMember().getId(), eachLike.getTargetId()));
    }
}
