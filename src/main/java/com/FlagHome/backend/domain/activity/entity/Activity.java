package com.FlagHome.backend.domain.activity.entity;

import com.FlagHome.backend.domain.activity.entity.enums.ActivityStatus;
import com.FlagHome.backend.domain.activity.entity.enums.ActivityType;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.global.common.BaseEntity;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Activity extends BaseEntity {
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
    private ActivityType type;

    @Column
    @Enumerated(EnumType.STRING)
    private ActivityStatus status;

    @Embedded
    private ActivityInfo info;

    @Builder
    public Activity(String name, String description, Member leader, ActivityType type, ActivityInfo info) {
        this.name = name;
        this.description = description;
        this.leader = leader;
        this.type = type;
        this.status = ActivityStatus.RECRUIT;
        this.info = info;
    }

    public static Activity of(Member member, Activity activity) {
        return Activity.builder()
                .name(activity.getName())
                .description(activity.getDescription())
                .leader(member)
                .type(activity.getType())
                .info(activity.getInfo())
                .build();
    }

    public void update(Activity activity) {
        this.name = activity.getName();
        this.description = activity.getDescription();
        this.info.updateInfo(activity);
    }

    public void isRecruiting() {
        if (this.status != ActivityStatus.RECRUIT) {
            throw new CustomException(ErrorCode.NOT_RECRUITMENT_ACTIVITY);
        }
    }

    public void closeRecruitment() {
        this.status = ActivityStatus.ON;
    }

    public void isInProgress() {
        if (this.status != ActivityStatus.ON) {
            throw new CustomException(ErrorCode.NOT_ON_ACTIVITY);
        }
    }

    public void finishActivity() {
        this.status = ActivityStatus.OFF;
    }
}