package com.FlagHome.backend.domain.post.dto;

import com.FlagHome.backend.domain.Status;
//import com.FlagHome.backend.domain.post.Category;
import com.FlagHome.backend.domain.post.entity.Post;
import com.FlagHome.backend.domain.reply.dto.ReplyDto;
import com.FlagHome.backend.domain.reply.entity.Reply;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    private List<ReplyDto> replyList;
    private long categoryId;
    private Status status;
    private long viewCount;

    public PostDto(Post postEntity) {
        this.id = postEntity.getId();
        this.userId = postEntity.getMember().getId();
        this.title = postEntity.getTitle();
        this.content = postEntity.getContent();
        this.categoryId = postEntity.getCategory().getId();
//        this.category = postEntity.getCategory();
        this.status = postEntity.getStatus();
        this.viewCount = postEntity.getViewCount();

        replyList = new ArrayList<>();
        for(Reply reply : postEntity.getReplyList())
            replyList.add(new ReplyDto(reply));
    }
}
