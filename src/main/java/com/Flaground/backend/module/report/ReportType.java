package com.Flaground.backend.module.report;

import com.Flaground.backend.global.common.CustomEnumDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = CustomEnumDeserializer.class)
public enum ReportType {
    음란물, 홍보, 도배, 욕설, 개인정보, 기타;
}
