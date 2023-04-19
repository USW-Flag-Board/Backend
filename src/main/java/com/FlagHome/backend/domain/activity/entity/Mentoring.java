package com.FlagHome.backend.domain.activity.entity;

import com.FlagHome.backend.domain.activity.entity.enums.ActivityType;
import com.FlagHome.backend.domain.activity.entity.enums.BookUsage;
import com.FlagHome.backend.domain.activity.entity.enums.Proceed;
import com.FlagHome.backend.domain.activity.entity.enums.ActivityStatus;
import com.FlagHome.backend.domain.activity.controller.dto.request.ActivityRequest;
import com.FlagHome.backend.domain.member.entity.Member;
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
public class Mentoring extends Activity {
    @Column
    private BookUsage bookUsage;

    @Column
    private String bookName;

    @Builder
    public Mentoring(String name, String description, Member leader, ActivityType activityType,
                     Proceed proceed, ActivityStatus status, int semester, BookUsage bookUsage, String bookName) {
        super(name, description, leader, activityType, proceed, status, semester);
        this.bookUsage = bookUsage;
        this.bookName = bookName;
    }

    public void updateMentoring(ActivityRequest activityRequest) {
        super.update(activityRequest);
        this.bookUsage = activityRequest.getBookUsage();
        this.bookName = activityRequest.getBookName();
    }

    public static Mentoring from(ActivityRequest activityRequest) {
        return Mentoring.builder()
                .name(activityRequest.getName())
                .description(activityRequest.getDescription())
                .proceed(activityRequest.getProceed())
                .bookUsage(activityRequest.getBookUsage())
                .bookName(activityRequest.getBookName())
                .activityType(ActivityType.MENTORING)
                .status(ActivityStatus.RECRUIT)
                .semester(LocalDateTime.now().getMonthValue())
                .build();
    }
}
