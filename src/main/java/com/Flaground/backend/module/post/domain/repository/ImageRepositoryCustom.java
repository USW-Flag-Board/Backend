package com.Flaground.backend.module.post.domain.repository;

import java.util.List;

public interface ImageRepositoryCustom {
    List<String> getKeysByPostId(Long postId);
}
