package com.FlagHome.backend.domain.activity.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CloseRecruitRequest {
    @Schema(name = "멤버 아이디 리스트", description = "모집 마감할 때 같이 활동할 유저 리스트")
    private List<String> loginIdList;

    @Builder
    public CloseRecruitRequest(List<String> loginIdList) {
        this.loginIdList = loginIdList;
    }
}
