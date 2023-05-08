package com.Flaground.backend.module.post.controller.mapper;

import com.Flaground.backend.module.post.controller.dto.request.PostRequest;
import com.Flaground.backend.module.post.domain.PostData;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface PostMapper {
    PostData toMetaData(PostRequest postRequest);
}
