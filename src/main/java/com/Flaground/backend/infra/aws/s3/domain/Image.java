package com.Flaground.backend.infra.aws.s3.domain;

import com.Flaground.backend.global.common.BaseEntity;
import com.Flaground.backend.module.post.domain.Post;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column
    private String key;

    public Image(Post post, String key) {
        this.post = post;
        this.key = key;
    }
}
