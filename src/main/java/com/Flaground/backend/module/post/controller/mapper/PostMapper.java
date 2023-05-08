package com.Flaground.backend.module.post.controller.mapper;

import com.Flaground.backend.module.post.domain.Post;
import com.Flaground.backend.module.post.controller.dto.request.PostRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface PostMapper {
    default Post mapFrom(PostRequest postRequest) {
        return Post.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .boardName(postRequest.getBoardName())
                .build();
    }
}
