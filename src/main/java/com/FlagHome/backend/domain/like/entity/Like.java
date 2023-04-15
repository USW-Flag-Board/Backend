package com.FlagHome.backend.domain.like.entity;

import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.global.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Like {
    /**
     * Version 1
     */
    /*@Id
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
    }*/

    /**
     * Version 2
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Like(Member member) {
        this.member = member;
        this.createdAt = LocalDateTime.now();
    }
}
