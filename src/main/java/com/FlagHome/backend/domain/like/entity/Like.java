package com.FlagHome.backend.domain.like.entity;

import com.FlagHome.backend.domain.like.enums.LikeType;
import com.FlagHome.backend.domain.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "target_id")
    private Long targetId;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type")
    private LikeType targetType;

    @Builder
    public Like(Long id, Member member, Long targetId, LikeType targetType) {
        this.id = id;
        this.member = member;
        this.targetId = targetId;
        this.targetType = targetType;
    }
}
