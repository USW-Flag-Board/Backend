package com.FlagHome.backend.domain.like.entity;

import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.reply.entity.Reply;
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
}
