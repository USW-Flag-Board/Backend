package com.FlagHome.backend.domain.activity.entity;

import com.FlagHome.backend.domain.activity.ActivityType;
import com.FlagHome.backend.domain.activity.BookUsage;
import com.FlagHome.backend.domain.activity.Proceed;
import com.FlagHome.backend.domain.activity.Status;
import com.FlagHome.backend.domain.activity.dto.ActivityRequest;
import com.FlagHome.backend.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Mentoring extends Activity {
    @Column
    private BookUsage bookUsage;

    @Column
    private String bookName;

    @Builder
    public Mentoring(String name, String description, Member leader, ActivityType activityType,
                     Proceed proceed, Status status, LocalDateTime season, BookUsage bookUsage, String bookName) {
        super(name, description, leader, activityType, proceed, status, season);
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
                .status(Status.RECRUIT)
                .season(LocalDateTime.now())
                .build();
    }
}
