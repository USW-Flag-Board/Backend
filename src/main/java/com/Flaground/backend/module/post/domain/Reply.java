package com.Flaground.backend.module.post.domain;

import com.Flaground.backend.global.common.BaseEntity;
import com.Flaground.backend.global.exception.CustomException;
import com.Flaground.backend.global.exception.ErrorCode;
import com.Flaground.backend.module.member.domain.Member;
import com.Flaground.backend.module.post.domain.enums.ReplyStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply extends BaseEntity implements Likeable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "post_id")
    private Long postId;

    @Column(length = 300)
    private String content;

    @Column(name = "likes")
    private int likeCount;

    @Column
    @Enumerated(EnumType.STRING)
    private ReplyStatus status;

    @Column(name = "is_edited")
    private boolean isEdited;

    @Builder
    public Reply(Member member, Long postId, String content) {
        this.member = member;
        this.postId = postId;
        this.content = content;
        this.likeCount = 0;
        this.status = ReplyStatus.NORMAL;
        this.isEdited = false;
    }

    public static Reply of(Member member, Long postId, String content) {
        return Reply.builder()
                .member(member)
                .postId(postId)
                .content(content)
                .build();
    }

    public void updateContent(String content) {
        this.content = content;
        this.isEdited = true;
    }

    @Override
    public int like() {
        return ++likeCount;
    }

    @Override
    public int dislike() {
        return --likeCount;
    }

    public void validateAuthor(Long memberId) {
        if (!member.getId().equals(memberId)) {
            throw new CustomException(ErrorCode.NOT_AUTHOR);
        }
    }
}