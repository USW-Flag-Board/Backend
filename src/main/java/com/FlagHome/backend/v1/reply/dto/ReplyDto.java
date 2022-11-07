package com.FlagHome.backend.v1.reply.dto;

import com.FlagHome.backend.v1.Status;
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
}
