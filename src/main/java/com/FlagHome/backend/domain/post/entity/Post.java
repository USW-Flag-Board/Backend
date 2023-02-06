package com.FlagHome.backend.domain.post.entity;

import com.FlagHome.backend.domain.common.BaseEntity;
import com.FlagHome.backend.domain.common.Status;
import com.FlagHome.backend.domain.board.entity.Board;
import com.FlagHome.backend.domain.like.entity.Like;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.reply.entity.Reply;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    private String title;

    @Column
    private String content;

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
    private Long viewCount;
}
