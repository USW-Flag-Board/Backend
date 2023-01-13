package com.FlagHome.backend.domain.activity.dto;

import com.FlagHome.backend.domain.activity.BookUsage;
import com.FlagHome.backend.domain.activity.Proceed;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateActivityRequest {
    @Schema(name = "프로젝트 이름", description = "한 문장으로 나타낼 이름", required = true)
    private String name;

    @Schema(name = "간단한 설명", description = "어떤 프로젝트인지 나타낼 설명", required = true)
    private String description;

    @Schema(name = "진행 방식", description = "대면 / 비대면", required = true)
    private Proceed proceed;

    @Schema(name = "Github Organization URL", description = "보여줄 Github Organization 주소, 수정 가능")
    private String githubLink;

    @Schema(name = "책 사용여부", description = "사용 / 미사용")
    private BookUsage bookUsage;

    @Schema(name = "사용할 책 이름", description = "사용에 체크했다면 입력 받기")
    private String bookName;
}
