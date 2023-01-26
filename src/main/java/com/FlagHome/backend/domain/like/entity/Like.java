package com.FlagHome.backend.domain.like.entity;

import com.FlagHome.backend.domain.like.enums.LikeType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "likes", indexes = @Index(name = "idx__userId", columnList = "user_id"))
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "target_id")
    private Long targetId;

    @Column(name = "target_type")
    private String targetType;

    @Builder
    public Like(Long id, Long userId, Long targetId, LikeType targetType) {
        this.id = id;
        this.userId = userId;
        this.targetId = targetId;
        this.targetType = targetType.toString();
    }
}
