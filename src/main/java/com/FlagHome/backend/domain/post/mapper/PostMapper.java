package com.FlagHome.backend.domain.post.mapper;

import com.FlagHome.backend.domain.post.entity.Post;
import com.FlagHome.backend.domain.post.controller.dto.CreatePostRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface PostMapper {
    default Post CreateRequestToEntity(CreatePostRequest createPostRequest) {
        return Post.builder()
                .title(createPostRequest.getTitle())
                .content(createPostRequest.getContent())
                .build();
    }
}
