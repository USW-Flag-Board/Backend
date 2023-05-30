package com.Flaground.backend.module.post.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "image_key")
    private String key;

    // todo: 멤버와의 연관관계 고민하기
    public Image(Long postId, String key) {
        this.postId = postId;
        this.key = key;
    }
}
