package com.FlagHome.backend.domain.activity.entity;

import com.FlagHome.backend.domain.BaseEntity;
import com.FlagHome.backend.domain.activity.ActivityType;
import com.FlagHome.backend.domain.activity.Proceed;
import com.FlagHome.backend.domain.activity.Status;
import com.FlagHome.backend.domain.activity.dto.ActivityRequest;
import com.FlagHome.backend.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
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

    public void setLeader(Member member) {
        this.leader = member;
    }

    public void closeRecruitment() {
        this.status = Status.ON;
    }
    public void update(ActivityRequest activityRequest) {
        this.name = activityRequest.getName();
        this.description = activityRequest.getDescription();
        this.proceed = activityRequest.getProceed();
    }
}