package com.FlagHome.backend.domain.activity.entity;

import com.FlagHome.backend.domain.activity.entity.enums.ActivityStatus;
import com.FlagHome.backend.domain.activity.entity.enums.ActivityType;
import com.FlagHome.backend.domain.activity.entity.enums.BookUsage;
import com.FlagHome.backend.domain.activity.entity.enums.Proceed;
import com.FlagHome.backend.domain.activity.controller.dto.ActivityRequest;
import com.FlagHome.backend.domain.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Study extends Activity {
    @Column
    private BookUsage bookUsage;

    @Column
    private String bookName;

    @Builder
    public Study(String name, String description, Member leader, ActivityType activityType,
                 Proceed proceed, ActivityStatus activityStatus, int semester, BookUsage bookUsage, String bookName) {
        super(name, description, leader, activityType, proceed, activityStatus, semester);
        this.bookUsage = bookUsage;
        this.bookName = bookName;
    }

    public void updateStudy(ActivityRequest activityRequest) {
        super.update(activityRequest);
        this.bookUsage = activityRequest.getBookUsage();
        this.bookName = activityRequest.getBookName();
    }

    public static Study from(ActivityRequest activityRequest) {
        return Study.builder()
                .name(activityRequest.getName())
                .description(activityRequest.getDescription())
                .proceed(activityRequest.getProceed())
                .bookUsage(activityRequest.getBookUsage())
                .bookName(activityRequest.getBookName())
                .activityType(ActivityType.STUDY)
                .activityStatus(ActivityStatus.RECRUIT)
                .semester(LocalDateTime.now().getMonthValue())
                .build();
    }
}