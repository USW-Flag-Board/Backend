package com.Flaground.backend.module.post.domain;

import com.Flaground.backend.global.common.BaseEntity;
import com.Flaground.backend.global.exception.CustomException;
import com.Flaground.backend.global.exception.ErrorCode;
import com.Flaground.backend.module.member.domain.Member;
import com.Flaground.backend.module.post.domain.enums.PostStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity implements Likeable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "board_name")
    private String boardName;

    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @OrderBy("id asc")
    private List<Reply> replies;

    @Column(name = "likes")
    private int likeCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PostStatus status;

    @Column(name = "views")
    private int viewCount;

    @Column(name = "is_edited")
    private boolean isEdited;

    @Builder
    public Post(Member member, String boardName, String title, String content) {
        this.member = member;
        this.boardName = boardName;
        this.title = title;
        this.content = content;
        this.replies = new ArrayList<>();
        this.likeCount = 0;
        this.status = PostStatus.NORMAL;
        this.viewCount = 0;
        this.isEdited = false;
    }

    public static Post of(Member member, PostData postData) {
        return Post.builder()
                .member(member)
                .boardName(postData.getBoardName())
                .title(postData.getTitle())
                .content(postData.getContent())
                .build();
    }

    public void updatePost(PostData postData) {
        this.boardName = postData.getBoardName();
        this.title = postData.getTitle();
        this.content = postData.getContent();
        this.isEdited = true;
    }

    public void addReply(Reply reply) {
        replies.add(reply);
    }

    public void deleteReply(Reply reply) {
        replies.remove(reply);
    }

    public void delete() {
        this.status = PostStatus.DELETED;
    }

    @Override
    public int like() {
        return ++likeCount;
    }

    @Override
    public int dislike() {
        return --likeCount;
    }

    public void isAccessible() {
        if (this.status == PostStatus.BANNED || this.status == PostStatus.DELETED) {
            throw new CustomException(ErrorCode.INACCESSIBLE_POST);
        }
        increaseViewCount();
    }

    public void validateAuthor(Long memberId) {
        if (!member.getId().equals(memberId)) {
            throw new CustomException(ErrorCode.NOT_AUTHOR);
        }
    }

    private void increaseViewCount() {
        this.viewCount += 1;
    }
}
