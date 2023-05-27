package com.Flaground.backend.module.report.domain.enums;

import com.Flaground.backend.global.common.CustomEnumDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@JsonDeserialize(using = CustomEnumDeserializer.class)
@RequiredArgsConstructor
public enum ReportCategory {
    음란물(5),
    홍보(5),
    도배(3),
    욕설(3),
    개인정보(10),
    기타(3);

    private final int penaltyPoint;
}
