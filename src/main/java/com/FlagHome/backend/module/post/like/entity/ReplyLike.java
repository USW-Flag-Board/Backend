package com.FlagHome.backend.module.post.like.entity;

import com.FlagHome.backend.module.member.domain.Member;
import com.FlagHome.backend.module.post.reply.entity.Reply;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyLike extends Like {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_id")
    private Reply reply;

    @Builder
    public ReplyLike(Member member, Reply reply) {
        super(member);
        this.reply = reply;
    }

    public static ReplyLike of(Member member, Reply reply) {
        return ReplyLike.builder()
                .member(member)
                .reply(reply)
                .build();
    }
}
