package com.Flaground.backend.module.post.domain;

import com.Flaground.backend.module.post.domain.enums.LikeType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @Column(name = "like_type")
    @Enumerated(EnumType.STRING)
    private LikeType likeType;

    @Builder
    public Like(Long memberId, Likeable likeable) {
        this.memberId = memberId;
        this.likeableId = likeable.getId();
        this.likeType = LikeType.from(likeable);
    }

    public static Like from(Long memberId, Likeable likeable) {
        return Like.builder()
                .memberId(memberId)
                .likeable(likeable)
                .build();
    }
}
