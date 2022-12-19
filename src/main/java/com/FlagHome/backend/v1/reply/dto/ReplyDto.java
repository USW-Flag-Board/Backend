package com.FlagHome.backend.v1.reply.dto;

import com.FlagHome.backend.v1.Status;
import com.FlagHome.backend.v1.reply.entity.Reply;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class ReplyDto {
    private long id;
    private long userId;
    private long postId;
    private String content;
    private long replyGroup;
    private long replyOrder;
    private long replyDepth;
    private Status status;

    public ReplyDto(Reply replyEntity) {
        this.id = replyEntity.getId();
        this.userId = replyEntity.getMember().getId();
        this.postId = replyEntity.getPost().getId();
        this.content = replyEntity.getContent();
        this.replyGroup = replyEntity.getReplyGroup();
        this.replyOrder = replyEntity.getReplyOrder();
        this.replyDepth = replyEntity.getReplyDepth();
        this.status = replyEntity.getStatus();
    }
}
