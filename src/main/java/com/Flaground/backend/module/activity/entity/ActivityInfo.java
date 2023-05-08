package com.Flaground.backend.module.activity.entity;

import com.Flaground.backend.module.activity.entity.enums.BookUsage;
import com.Flaground.backend.module.activity.entity.enums.Proceed;
import com.Flaground.backend.module.activity.entity.enums.Semester;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(name = "github_url")
    private String githubURL;

    @Column(name = "book_usage")
    @Enumerated(EnumType.STRING)
    private BookUsage bookUsage;

    @Column(name = "book_name")
    private String bookName;

    @Builder
    public ActivityInfo(Proceed proceed, String githubURL, BookUsage bookUsage, String bookName) {
        this.semester = Semester.findSemester(LocalDateTime.now().getMonthValue());
        this.proceed = proceed;
        this.githubURL = githubURL;
        this.bookUsage = bookUsage;
        this.bookName = bookName;
    }

    public void updateInfo(Activity activity) {
        this.proceed = activity.getInfo().getProceed();
        this.githubURL = activity.getInfo().getGithubURL();
        this.bookUsage = activity.getInfo().getBookUsage();
        this.bookName = activity.getInfo().getBookName();
    }
}
