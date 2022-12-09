package com.FlagHome.backend.v1.post.entity;

import com.FlagHome.backend.v1.BaseEntity;
import com.FlagHome.backend.v1.Status;
import com.FlagHome.backend.v1.post.Category;
import com.FlagHome.backend.v1.member.entity.Member;
import com.FlagHome.backend.v1.reply.entity.Reply;
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

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column
    private Long viewCount;
}
