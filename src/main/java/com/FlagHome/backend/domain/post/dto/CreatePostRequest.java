package com.FlagHome.backend.domain.post.dto;

import com.FlagHome.backend.global.common.Status;
import com.FlagHome.backend.domain.post.entity.Post;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class CreatePostRequest {
    private long id;
    private long userId;
    private String title;
    private String content;
    private String imgUrl;
    private String fileUrl;
    private long boardId;
    private Status status;


    public CreatePostRequest(Post postEntity) {
        this.id = postEntity.getId();
        this.userId = postEntity.getMember().getId();
        this.title = postEntity.getTitle();
        this.content = postEntity.getContent();
        this.imgUrl = postEntity.getImgUrl();
        this.fileUrl = postEntity.getFileUrl();
        this.boardId = postEntity.getBoard().getId();
        this.status = postEntity.getStatus();
    }
}

