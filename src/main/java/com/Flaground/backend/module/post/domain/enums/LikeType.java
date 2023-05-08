package com.Flaground.backend.module.post.domain.enums;

import com.Flaground.backend.module.post.domain.Likeable;
import com.Flaground.backend.module.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LikeType {
    POST, REPLY;

    public static LikeType from(Likeable likeable) {
        return likeable instanceof Post ? POST : REPLY;
    }
}
