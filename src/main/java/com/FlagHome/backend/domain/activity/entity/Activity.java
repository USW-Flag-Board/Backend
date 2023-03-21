package com.FlagHome.backend.domain.activity.entity;

import com.FlagHome.backend.domain.activity.controller.dto.ActivityRequest;
import com.FlagHome.backend.domain.activity.entity.enums.ActivityType;
import com.FlagHome.backend.domain.activity.entity.enums.Proceed;
import com.FlagHome.backend.domain.activity.entity.enums.Status;
import com.FlagHome.backend.domain.member.Member;
import com.FlagHome.backend.global.common.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
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
    private Status status;

    @Column
    private String season;

    public Activity(String name, String description, Member leader, ActivityType activityType,
                    Proceed proceed, Status status, LocalDateTime season) {
        this.name = name;
        this.description = description;
        this.leader = leader;
        this.activityType = activityType;
        this.proceed = proceed;
        this.status = status;
        this.season = getSeason(season);
    }

    public void updateLeader(Member member) {
        this.leader = member;
    }

    public void closeRecruitment() {
        this.status = Status.ON;
    }

    public void reopenRecruitment() {
        this.status = Status.RECRUIT;
    }

    public void finishActivity() {
        this.status = Status.OFF;
    }

    public void update(ActivityRequest activityRequest) {
        this.name = activityRequest.getName();
        this.description = activityRequest.getDescription();
        this.proceed = activityRequest.getProceed();
    }

    protected String getSeason(LocalDateTime now) { // 꼭 개선하기
        final int month = now.getMonthValue();

        if (3 <= month && month < 6) {
            return "1학기";
        } else if (6 <= month && month < 9) {
            return "여름방학";
        } else if (9 <= month && month < 12) {
            return "2학기";
        } else {
            return "겨울방학";
        }
    }
}