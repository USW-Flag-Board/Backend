package com.FlagHome.backend.domain.post.entity;

import com.FlagHome.backend.domain.board.entity.Board;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.reply.entity.Reply;
import com.FlagHome.backend.global.common.BaseEntity;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
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
public class Post extends BaseEntity {
    /**
     * Version 1
     */
    /* @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private String imgUrl;

    @Column
    private String fileUrl;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> replyList;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likeList;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column
    private Long viewCount; */

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
    @JoinColumn(name = "board_id")
    private Board board;

    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @OneToMany(mappedBy = "post" ,cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> replyList;

    @Column
    @Enumerated(EnumType.STRING)
    private PostStatus status;

    @Column
    private int viewCount;

    @Column(name = "is_edited")
    private boolean isEdited;

    @Builder
    public Post(Member member, Board board, String title, String content) {
        this.member = member;
        this.board = board;
        this.title = title;
        this.content = content;
        this.replyList = new ArrayList<>();
        this.status = PostStatus.NORMAL;
        this.viewCount = 0;
        this.isEdited = false;
    }

    public static Post of(Member member, Board board, Post post) {
        return Post.builder()
                .member(member)
                .title(post.getTitle())
                .content(post.getContent())
                .board(board)
                .build();
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void updatePost(Post post) {
        this.board = post.getBoard();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.isEdited = true;
    }

    public void delete() {
        this.status = PostStatus.DELETED;
    }

    public void isAccessible() {
        if (this.status == PostStatus.BANNED || this.status == PostStatus.DELETED) {
            throw new CustomException(ErrorCode.INACCESSIBLE_POST);
        }
    }
}
