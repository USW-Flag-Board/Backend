package com.FlagHome.backend.infra.aws.s3.entity;

import com.FlagHome.backend.module.post.entity.Post;
import com.FlagHome.backend.global.common.BaseEntity;
import com.FlagHome.backend.infra.aws.s3.entity.enums.FileStatus;
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
    private String url;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private FileStatus status;

    @Builder
    public File(Post post, String url, FileStatus status) {
        this.post = post;
        this.url = url;
        this.status = status;
    }
}