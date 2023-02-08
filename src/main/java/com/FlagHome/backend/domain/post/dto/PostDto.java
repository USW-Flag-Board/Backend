package com.FlagHome.backend.domain.post.dto;

import com.FlagHome.backend.domain.common.Status;
import com.FlagHome.backend.domain.like.entity.Like;
import com.FlagHome.backend.domain.like.entity.LikeDto;
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
public class PostDto {
    private long id;
    private long userId;
    private String title;
    private String content;
    private String imgUrl;
    private String fileUrl;
    private String memberName;
    private List<ReplyDto> replyList;
    private List<LikeDto> likeList;
    private long boardId;
    private Status status;
    private long viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PostDto(Post postEntity) {
        this.id = postEntity.getId();
        this.userId = postEntity.getMember().getId();
        this.title = postEntity.getTitle();
        this.content = postEntity.getContent();
        this.imgUrl = postEntity.getImgUrl();
        this.fileUrl = postEntity.getFileUrl();
        this.boardId = postEntity.getBoard().getId();
        this.status = postEntity.getStatus();
        this.viewCount = postEntity.getViewCount();

        this.replyList = new ArrayList<>();
        for(Reply reply : postEntity.getReplyList())
            this.replyList.add(new ReplyDto(reply));

        this.likeList = new ArrayList<>();
        for(Like eachLike : postEntity.getLikeList())
            this.likeList.add(new LikeDto(eachLike.getMember().getId(), eachLike.getTargetId()));
    }

    // Projection용 생성자
    public PostDto(long id, String title, LocalDateTime createdAt, long boardId, String memberName, long viewCount) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.boardId = boardId;
        this.memberName = memberName;
        this.viewCount = viewCount;
    }
}
