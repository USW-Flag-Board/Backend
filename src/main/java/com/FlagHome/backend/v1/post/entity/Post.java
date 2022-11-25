package com.FlagHome.backend.v1.post.entity;

import com.FlagHome.backend.v1.BaseEntity;
import com.FlagHome.backend.v1.Status;
import com.FlagHome.backend.v1.post.Category;
import com.FlagHome.backend.v1.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Member member;

    @Column
    private String title;

    @Column
    private String content;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column
    private Long viewCount;
}
