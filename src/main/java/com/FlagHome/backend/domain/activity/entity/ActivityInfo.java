package com.FlagHome.backend.domain.activity.entity;

import com.FlagHome.backend.domain.activity.entity.enums.BookUsage;
import com.FlagHome.backend.domain.activity.entity.enums.Proceed;
import com.FlagHome.backend.domain.activity.entity.enums.Semester;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ActivityInfo {
    @Column
    @Enumerated(EnumType.STRING)
    private Semester semester;

    @Column
    @Enumerated(EnumType.STRING)
    private Proceed proceed;

    @Column
    @Nullable
    private String githubURL;

    @Column
    @Enumerated(EnumType.STRING)
    @Nullable
    private BookUsage bookUsage;

    @Column
    @Nullable
    private String bookName;

    @Builder
    public ActivityInfo(Proceed proceed, @Nullable String githubURL, @Nullable BookUsage bookUsage, @Nullable String bookName) {
        this.semester = Semester.findSemester(LocalDateTime.now().getMonthValue());
        this.proceed = proceed;
        this.githubURL = githubURL;
        this.bookUsage = bookUsage;
        this.bookName = bookName;
    }
}
