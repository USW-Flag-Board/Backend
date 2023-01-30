package com.FlagHome.backend.domain.like.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LikeDto {
    private Long userId;
    private Long targetId;
    private String targetType;
}
