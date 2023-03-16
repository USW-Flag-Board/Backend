package com.FlagHome.backend.domain.member.dto;

import com.FlagHome.backend.domain.member.Major;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SearchMemberResponse {
    @Schema(name = "아이디")
    private Long id;
    @Schema(name = "회원 이름", example = "홍길동")
    private String name;

    @Schema(name = "회원 전공", example = "정보보호")
    private String major;

    @Builder
    public SearchMemberResponse(Long id, String name, Major major) {
        this.id = id;
        this.name =  name;
        this.major = String.valueOf(major);
    }
}
