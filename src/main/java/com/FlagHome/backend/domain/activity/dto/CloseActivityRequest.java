package com.FlagHome.backend.domain.activity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CloseActivityRequest {
    @Schema(name = "활동 고유번호", required = true)
    private long id;

    @Schema(name = "활동에 등록할 멤버 리스트", required = true, description = "모집 마감이 될 때 활동장이 멤버를 선택한다.")
    private List<String> memberList;
}
