package com.Flaground.backend.module.activity.activityapply.entity;

import com.Flaground.backend.module.activity.entity.Activity;
import com.Flaground.backend.module.member.domain.Member;
import lombok.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id")
    private Activity activity;

    @Column(name = "apply_time")
    private LocalDateTime applyTime;

    @Builder
    public ActivityApply(Member member, Activity activity) {
        this.member = member;
        this.activity = activity;
        this.applyTime = LocalDateTime.now();
    }

    public static ActivityApply of(Member member, Activity activity) {
        return ActivityApply.builder()
                .member(member)
                .activity(activity)
                .build();
    }
}
