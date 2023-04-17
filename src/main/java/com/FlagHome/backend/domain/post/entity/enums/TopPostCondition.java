package com.FlagHome.backend.domain.post.entity.enums;

import com.querydsl.core.types.OrderSpecifier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.FlagHome.backend.domain.post.entity.QPost.post;

@Getter
@RequiredArgsConstructor
public enum TopPostCondition {
    like(post.likeCount.desc()),
    latest(post.createdAt.asc());

    private final OrderSpecifier<?> order;
}
