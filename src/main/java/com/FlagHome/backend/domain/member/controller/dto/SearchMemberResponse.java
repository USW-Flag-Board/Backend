package com.FlagHome.backend.domain.member.controller.dto;

import com.FlagHome.backend.domain.member.Major;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchMemberResponse {
    @Schema(name = "회원 이름", example = "홍길동")
    private String name;

    @Schema(name = "회원 전공", example = "정보보호")
    private String major;

    @Builder
    @QueryProjection
    public SearchMemberResponse(String name, Major major) {
        this.name =  name;
        this.major = String.valueOf(major);
    }
}
