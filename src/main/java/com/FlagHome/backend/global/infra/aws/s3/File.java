package com.FlagHome.backend.global.infra.aws.s3;

import com.FlagHome.backend.domain.post.entity.Post;
import com.FlagHome.backend.global.common.BaseEntity;
import com.FlagHome.backend.global.common.Status;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column
    private String link;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public File(Long id, Post post, String link, Status status) {
        this.id = id;
        this.post = post;
        this.link = link;
        this.status = status;
    }
}