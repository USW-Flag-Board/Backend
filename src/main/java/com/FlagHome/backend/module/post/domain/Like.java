package com.FlagHome.backend.module.post.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "likeable_id")
    private Long likeableId;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Builder
    public Like(Long memberId, Long likeableId) {
        this.memberId = memberId;
        this.likeableId = likeableId;
        this.createAt = LocalDateTime.now();
    }

    public static Like from(Long memberId, Long likeableId) {
        return Like.builder()
                .memberId(memberId)
                .likeableId(likeableId)
                .build();
    }
}
