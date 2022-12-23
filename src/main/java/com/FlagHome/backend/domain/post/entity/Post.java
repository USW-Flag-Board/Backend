package com.FlagHome.backend.domain.post.entity;

import com.FlagHome.backend.domain.BaseEntity;
import com.FlagHome.backend.domain.Status;
import com.FlagHome.backend.domain.category.entity.Category;
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

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column
    private Long viewCount;
}
