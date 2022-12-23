package com.FlagHome.backend.domain.category.entity;

import com.FlagHome.backend.domain.post.entity.Post;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column
    private String koreanName;

    @Column
    private String englishName;

    @Column
    private Long categoryDepth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private final List<Category> children = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "category_id")
    private List<Post> postList;
}