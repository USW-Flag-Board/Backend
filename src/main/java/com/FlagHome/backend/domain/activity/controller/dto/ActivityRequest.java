package com.FlagHome.backend.domain.activity.controller.dto;

import com.FlagHome.backend.domain.activity.ActivityType;
import com.FlagHome.backend.domain.activity.BookUsage;
import com.FlagHome.backend.domain.activity.Proceed;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ActivityRequest {
    @Schema(name = "활동 이름", description = "한 문장으로 나타낼 이름", required = true)
    private String name;

    @Schema(name = "간단한 설명", description = "어떤 프로젝트인지 나타낼 설명", required = true)
    private String description;

    @Schema(name = "진행 방식", description = "대면 / 비대면", required = true)
    private Proceed proceed;

    @Schema(name = "활동 타입", description = "프로젝트 / 스터디 / 멘토링 중 하나", required = true, example = "PROJECT, STUDY, MENTORING")
    private ActivityType activityType;

    @Schema(name = "Github Organization URL", description = "보여줄 Github Organization 주소, 수정 가능")
    private String githubLink;

    @Schema(name = "책 사용여부", description = "사용 / 미사용")
    private BookUsage bookUsage;

    @Schema(name = "사용할 책 이름", description = "사용에 체크했다면 입력 받기")
    private String bookName;

    @Builder
    public ActivityRequest(String name, String description, Proceed proceed, ActivityType activityType,
                           String githubLink, BookUsage bookUsage, String bookName) {
        this.name = name;
        this.description = description;
        this.proceed = proceed;
        this.activityType = activityType;
        this.githubLink = githubLink;
        this.bookUsage = bookUsage;
        this.bookName = bookName;
    }
}
