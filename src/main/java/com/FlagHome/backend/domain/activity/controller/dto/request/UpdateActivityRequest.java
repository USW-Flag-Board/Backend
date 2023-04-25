package com.FlagHome.backend.domain.activity.controller.dto.request;

import com.FlagHome.backend.domain.activity.entity.enums.BookUsage;
import com.FlagHome.backend.domain.activity.entity.enums.Proceed;
import com.FlagHome.backend.global.annotation.EnumFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateActivityRequest implements ActivityInfoRequest {
    @Schema(name = "활동 이름")
    @NotBlank
    private String name;

    @Schema(name = "간단한 설명")
    @NotBlank
    private String description;

    @Schema(name = "진행 방식", example = "ONLINE, OFFLINE, BOTH")
    @EnumFormat(enumClass = Proceed.class)
    private Proceed proceed;

    @Schema(name = "깃헙 Organization 주소", description = "프로젝트만 사용하는 값")
    @NotNull
    private String githubURL;

    @Schema(name = "책 사용여부", description = "스터디/멘토링에서 사용하는 값", example = "USE, NOT_USE")
    @EnumFormat(enumClass = BookUsage.class)
    private BookUsage bookUsage;

    @Schema(name = "사용할 책 이름", description = "스터디/멘토링에서 사용하는 값")
    @NotNull
    private String bookName;

    @Builder
    public UpdateActivityRequest(String name, String description, Proceed proceed,
                                 String githubURL, BookUsage bookUsage, String bookName) {
        this.name = name;
        this.description = description;
        this.proceed = proceed;
        this.githubURL = githubURL;
        this.bookUsage = bookUsage;
        this.bookName = bookName;
    }
}
