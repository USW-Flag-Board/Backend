package com.FlagHome.backend.domain.post.controller.dto;

import com.FlagHome.backend.global.common.Status;
import com.FlagHome.backend.domain.post.entity.Post;
import com.FlagHome.backend.domain.reply.dto.ReplyDto;
import com.FlagHome.backend.domain.reply.entity.Reply;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class GetPostResponse {
    private long id;
    private long userId;
    private String title;
    private String content;
    private String imgUrl;
    private String fileUrl;
    private List<ReplyDto> replyList;
    private long boardId;
    private Status status;
    private long viewCount;
    private long likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public GetPostResponse(Post postEntity) {
        this.id = postEntity.getId();
        this.userId = postEntity.getMember().getId();
        this.title = postEntity.getTitle();
        this.content = postEntity.getContent();
        this.imgUrl = postEntity.getImgUrl();
        this.fileUrl = postEntity.getFileUrl();
        this.boardId = postEntity.getBoard().getId();
        this.status = postEntity.getStatus();
        this.viewCount = postEntity.getViewCount();
        this.createdAt = postEntity.getCreatedAt();
        //this.likeCount = postEntity.getLikeList();

        replyList = new ArrayList<>();
        for(Reply reply : postEntity.getReplyList())
            replyList.add(new ReplyDto(reply));
    }
}

