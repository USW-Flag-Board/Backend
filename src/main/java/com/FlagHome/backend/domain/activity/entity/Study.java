package com.FlagHome.backend.domain.activity.entity;

import com.FlagHome.backend.domain.activity.ActivityType;
import com.FlagHome.backend.domain.activity.BookUsage;
import com.FlagHome.backend.domain.activity.Proceed;
import com.FlagHome.backend.domain.activity.Status;
import com.FlagHome.backend.domain.activity.dto.CreateActivityRequest;
import com.FlagHome.backend.domain.activity.dto.UpdateActivityRequest;
import com.FlagHome.backend.domain.member.entity.Member;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@NoArgsConstructor
public class Study extends Activity {
    @Column
    private BookUsage bookUsage;

    @Column
    private String bookName;

    @Builder
    public Study(Long id, String name, String description, Member leader, ActivityType activityType,
                 Proceed proceed, Status status, BookUsage bookUsage, String bookName) {
        super(id, name, description, leader, activityType, proceed, status);
        this.bookUsage = bookUsage;
        this.bookName = bookName;
    }

    public void updateStudy(UpdateActivityRequest updateActivityRequest) {
        super.update(updateActivityRequest);
        this.bookUsage = updateActivityRequest.getBookUsage();
        this.bookName = updateActivityRequest.getBookName();
    }

    public static Study from(CreateActivityRequest createActivityRequest) {
        return Study.builder()
                .name(createActivityRequest.getName())
                .description(createActivityRequest.getDescription())
                .proceed(createActivityRequest.getProceed())
                .bookUsage(createActivityRequest.getBookUsage())
                .bookName(createActivityRequest.getBookName())
                .activityType(ActivityType.STUDY)
                .status(Status.RECRUIT)
                .build();
    }
}