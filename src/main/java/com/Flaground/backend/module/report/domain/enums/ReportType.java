package com.Flaground.backend.module.report.domain.enums;

import com.Flaground.backend.global.common.CustomEnumDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonDeserialize(using = CustomEnumDeserializer.class)
public enum ReportType {
    MEMBER("프로필"),
    POST("게시글"),
    REPLY("댓글");

    private final String type;
}
