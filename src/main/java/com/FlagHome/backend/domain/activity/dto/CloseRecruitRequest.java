package com.FlagHome.backend.domain.activity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CloseRecruitRequest {
    @Schema(name = "멤버 아이디 리스트", description = "모집 마감할 때 같이 활동할 유저 리스트")
    private List<String> loginIdList;
}
