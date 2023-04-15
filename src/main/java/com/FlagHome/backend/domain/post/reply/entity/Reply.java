package com.FlagHome.backend.domain.post.reply.entity;

import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.post.entity.Post;
import com.FlagHome.backend.domain.post.like.Likeable;
import com.FlagHome.backend.global.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply extends BaseEntity implements Likeable {
    /**
     * Version 1
     */
    /* @Id
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
    private Status status; */

    /**
     * Version 2
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(length = 300)
    private String content;

    @Column(name = "likes")
    private int likeCount;

    @Column
    @Enumerated(EnumType.STRING)
    private ReplyStatus status;

    @Builder
    public Reply(Member member, Post post, String content) {
        this.member = member;
        this.post = post;
        this.content = content;
        this.likeCount = 0;
        this.status = ReplyStatus.NORMAL;
    }

    public void renewContent(String content) {
        this.content = content;
    }

    @Override
    public void increaseLikeCount() {
        this.likeCount += 1;
    }

    @Override
    public void decreaseLikeCount() {
        this.likeCount -= 1;
    }

    public static Reply of(Member member, Post post, String content) {
        return Reply.builder()
                .member(member)
                .post(post)
                .content(content)
                .build();
    }
}