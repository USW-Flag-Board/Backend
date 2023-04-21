package com.FlagHome.backend.domain.activity.entity;

import com.FlagHome.backend.domain.activity.controller.dto.request.ActivityRequest;
import com.FlagHome.backend.domain.activity.entity.enums.ActivityStatus;
import com.FlagHome.backend.domain.activity.entity.enums.ActivityType;
import com.FlagHome.backend.domain.activity.entity.enums.Proceed;
import com.FlagHome.backend.domain.activity.entity.enums.Semester;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.global.common.BaseEntity;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Activity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id")
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member leader;

    @Column
    @Enumerated(EnumType.STRING)
    private ActivityType activityType;

    @Column
    @Enumerated(EnumType.STRING)
    private Proceed proceed;

    @Column
    @Enumerated(EnumType.STRING)
    private ActivityStatus status;

    @Column
    @Enumerated(EnumType.STRING)
    private Semester semester;

    public Activity(String name, String description, Member leader, ActivityType activityType,
                    Proceed proceed, int semester) {
        this.name = name;
        this.description = description;
        this.leader = leader;
        this.activityType = activityType;
        this.proceed = proceed;
        this.status = ActivityStatus.RECRUIT;
        this.semester = Semester.findSemester(semester);
    }

    public void changeLeader(Member member) {
        this.leader = member;
    }

    public void isRecruitment() {
        if (this.status != ActivityStatus.RECRUIT) {
            throw new CustomException(ErrorCode.NOT_RECRUITMENT_ACTIVITY);
        }
    }

    public void closeRecruitment() {
        this.status = ActivityStatus.ON;
    }

    public void isOn() {
        if (this.status != ActivityStatus.ON) {
            throw new CustomException(ErrorCode.NOT_ON_ACTIVITY);
        }
    }

    public void finishActivity() {
        this.status = ActivityStatus.OFF;
    }

    public void update(ActivityRequest activityRequest) {
        this.name = activityRequest.getName();
        this.description = activityRequest.getDescription();
        this.proceed = activityRequest.getProceed();
    }
}