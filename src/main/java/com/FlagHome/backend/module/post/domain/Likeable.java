package com.FlagHome.backend.module.post.domain;

public interface Likeable {
    Long getMemberId();

    Long getId();

    int like();

    int dislike();
}
