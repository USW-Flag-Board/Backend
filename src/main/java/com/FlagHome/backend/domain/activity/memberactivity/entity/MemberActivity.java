package com.FlagHome.backend.domain.activity.memberactivity.entity;

import com.FlagHome.backend.global.common.BaseEntity;
import com.FlagHome.backend.domain.activity.entity.Activity;
import com.FlagHome.backend.domain.member.entity.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberActivity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id")
    private Activity activity;

    @Builder
    public MemberActivity(Member member, Activity activity) {
        this.member = member;
        this.activity = activity;
    }

    public static MemberActivity of(Member member, Activity activity) {
        return MemberActivity.builder()
                .member(member)
                .activity(activity)
                .build();
    }
}
