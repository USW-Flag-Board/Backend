package com.FlagHome.backend.v1.post.file.entity;

import com.FlagHome.backend.v1.BaseEntity;

import com.FlagHome.backend.v1.Status;
import com.FlagHome.backend.v1.post.entity.Post;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
}
