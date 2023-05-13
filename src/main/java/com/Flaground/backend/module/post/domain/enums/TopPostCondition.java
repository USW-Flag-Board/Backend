package com.Flaground.backend.module.post.domain.enums;

import com.querydsl.core.types.OrderSpecifier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.Flaground.backend.module.post.domain.QPost.post;

@Getter
@RequiredArgsConstructor
public enum TopPostCondition {
    like(post.likeCount.desc()),
    latest(post.createdAt.desc());

    private final OrderSpecifier<?> order;

    public boolean isLikeCondition() {
        return this == TopPostCondition.like;
    }
}
