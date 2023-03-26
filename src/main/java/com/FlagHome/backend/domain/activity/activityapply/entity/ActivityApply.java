package com.FlagHome.backend.domain.activity.activityapply.entity;

import com.FlagHome.backend.domain.activity.entity.Activity;
import com.FlagHome.backend.domain.member.Member;
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

    @Column
    private LocalDateTime applyTime;

    @Builder
    public ActivityApply(Member member, Activity activity, LocalDateTime applyTime) {
        this.member = member;
        this.activity = activity;
        this.applyTime = applyTime;
    }
}
