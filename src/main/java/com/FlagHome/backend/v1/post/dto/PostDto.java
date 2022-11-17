package com.FlagHome.backend.v1.post.dto;

import com.FlagHome.backend.v1.Status;
import com.FlagHome.backend.v1.post.Category;
import com.FlagHome.backend.v1.post.entity.Post;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class PostDto {
    private long id;
    private long userId;
    private String title;
    private String content;
    private Category category;
    private Status status;
    private long viewCount;

    public PostDto(Post postEntity) {
        this.id = postEntity.getId();
        this.userId = postEntity.getUser().getId();
        this.title = postEntity.getTitle();
        this.content = postEntity.getContent();
        this.category = postEntity.getCategory();
        this.status = postEntity.getStatus();
        this.viewCount = postEntity.getViewCount();
    }
}
