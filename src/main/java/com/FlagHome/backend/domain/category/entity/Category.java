package com.FlagHome.backend.domain.category.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}