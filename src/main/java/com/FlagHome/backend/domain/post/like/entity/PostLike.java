package com.FlagHome.backend.domain.post.like.entity;

import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.post.entity.Post;
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
public class PostLike extends Like {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public PostLike(Member member, Post post) {
        super(member);
        this.post = post;
    }

    public static PostLike of(Member member, Post post) {
        return PostLike.builder()
                .member(member)
                .post(post)
                .build();
    }
}
