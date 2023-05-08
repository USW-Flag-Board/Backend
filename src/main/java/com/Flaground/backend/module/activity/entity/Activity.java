package com.Flaground.backend.module.activity.entity;

import com.Flaground.backend.module.activity.entity.enums.ActivityStatus;
import com.Flaground.backend.module.activity.entity.enums.ActivityType;
import com.Flaground.backend.module.member.domain.Member;
import com.Flaground.backend.global.common.BaseEntity;
import com.Flaground.backend.global.exception.CustomException;
import com.Flaground.backend.global.exception.ErrorCode;
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

    public void closeRecruitment() {
        this.status = ActivityStatus.ON;
    }

    public void finishActivity() {
        this.status = ActivityStatus.OFF;
    }

    public void isRecruiting() {
        if (this.status != ActivityStatus.RECRUIT) {
            throw new CustomException(ErrorCode.NOT_RECRUITMENT_ACTIVITY);
        }
    }

    public void isInProgress() {
        if (this.status != ActivityStatus.ON) {
            throw new CustomException(ErrorCode.NOT_ON_ACTIVITY);
        }
    }

    public void validateLeader(Long memberId) {
        if (!leader.getId().equals(memberId)) {
            throw new CustomException(ErrorCode.NOT_ACTIVITY_LEADER);
        }
    }
}