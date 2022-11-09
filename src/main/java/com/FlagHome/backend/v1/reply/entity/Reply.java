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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column
    private String content;

    @Column(name = "reply_group")
    private Long replyGroup;

    @Column(name = "reply_order")
    private Long replyOrder;

    @Column(name = "reply_depth")
    private Long replyDepth;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
}

// branch test