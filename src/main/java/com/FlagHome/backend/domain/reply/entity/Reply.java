package com.FlagHome.backend.domain.reply.entity;

import com.FlagHome.backend.domain.common.BaseEntity;
import com.FlagHome.backend.domain.common.Status;
import com.FlagHome.backend.domain.like.entity.Like;
import com.FlagHome.backend.domain.post.entity.Post;
import com.FlagHome.backend.domain.member.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likeList;

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