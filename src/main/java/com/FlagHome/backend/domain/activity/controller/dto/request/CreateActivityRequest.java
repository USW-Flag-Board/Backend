package com.FlagHome.backend.domain.activity.controller.dto.request;

import com.FlagHome.backend.domain.activity.entity.enums.ActivityType;
import com.FlagHome.backend.domain.activity.entity.enums.BookUsage;
import com.FlagHome.backend.domain.activity.entity.enums.Proceed;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateActivityRequest implements InfoRequest {
    @Schema(name = "활동 이름")
    @NotBlank
    private String name;

    @Schema(name = "간단한 설명")
    @NotBlank
    private String description;

    @Schema(name = "진행 방식", example = "ONLINE, OFFLINE, BOTH")
    @NotNull
    private Proceed proceed;

    @Schema(name = "활동 타입", example = "PROJECT, STUDY, MENTORING")
    @NotNull
    private ActivityType type;

    @Schema(name = "깃헙 Organization 주소", description = "프로젝트만 사용하는 값")
    @Nullable
    private String githubURL;

    @Schema(name = "책 사용여부", description = "스터디/멘토링에서 사용하는 값", example = "USE, NOT_USE")
    @Nullable
    private BookUsage bookUsage;

    @Schema(name = "사용할 책 이름", description = "스터디/멘토링에서 사용하는 값")
    @Nullable
    private String bookName;

    @Builder
    public CreateActivityRequest(String name, String description, Proceed proceed, ActivityType type,
                                 @Nullable String githubURL, @Nullable BookUsage bookUsage, @Nullable String bookName) {
        this.name = name;
        this.description = description;
        this.proceed = proceed;
        this.type = type;
        this.githubURL = githubURL;
        this.bookUsage = bookUsage;
        this.bookName = bookName;
    }
}
