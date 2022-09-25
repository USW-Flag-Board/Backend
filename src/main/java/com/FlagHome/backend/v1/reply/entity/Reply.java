package com.FlagHome.backend.v1.reply.entity;

import com.FlagHome.backend.v1.BaseEntity;
import com.FlagHome.backend.v1.Status;
import com.FlagHome.backend.v1.post.entity.Post;
import com.FlagHome.backend.v1.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column
    private String content;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
}
