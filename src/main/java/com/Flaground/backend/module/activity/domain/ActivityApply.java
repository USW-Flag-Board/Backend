package com.Flaground.backend.module.activity.domain;

import com.Flaground.backend.module.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActivityApply {
    @Id
    @Column(name = "activity_apply_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "activity_id")
    private Long activityId;

    @Column(name = "apply_time", updatable = false)
    private LocalDateTime applyTime;

    @Builder
    public ActivityApply(Member member, Long activityId) {
        this.member = member;
        this.activityId = activityId;
        this.applyTime = LocalDateTime.now();
    }

    public static ActivityApply of(Member member, Long activityId) {
        return ActivityApply.builder()
                .member(member)
                .activityId(activityId)
                .build();
    }
}
