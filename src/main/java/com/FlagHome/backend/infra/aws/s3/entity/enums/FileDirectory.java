package com.FlagHome.backend.infra.aws.s3.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileDirectory {
    PROFILE("avatar"),
    POST("post");

    private final String directory;
}
