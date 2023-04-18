package com.FlagHome.backend.domain.post.mapper;

import com.FlagHome.backend.domain.post.entity.Post;
import com.FlagHome.backend.domain.post.controller.dto.request.PostRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface PostMapper {
    default Post toEntity(PostRequest postRequest) {
        return Post.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .build();
    }
}
