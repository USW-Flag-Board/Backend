package com.FlagHome.backend.domain.category.entity;

import com.FlagHome.backend.domain.post.entity.Post;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
//@NoArgsConstructor(access = AccessLevel.PROTECTED) //헷갈려서 일단 주석처리 해놨습니다(22.12.31 강지은)
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

    /*리팩토링을 진행하던 중 LazyInitializationException이 발생했습니다.
    영속성 컨텍스트가 종료되어 발생하는 에러라고하는데 (FetchType.Lazy) - Join을 해서 가져오는 FetchType.EAGER을 사용하면 잘 작동합니다.
    1) LAZY + 어디에 @Transaction을 넣어야 하는지
    2) EAGER를 사용하는게 좋은지
    아직 잘 모르겠어서 좀더 학습을 한 이후에 개선해야할 것 같습니다 (22.12.31 강지은) */
    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    private final List<Category> children = new ArrayList<>();

    @OneToMany(mappedBy = "category")
    private List<Post> postList;

    @Builder //PostControllerTest에 빌더 사용한게 남아있어서 남겼습니다. (22.12.31 강지은)
    public Category(String koreanName, String englishName, Long categoryDepth, Category parent) {
        this.koreanName = koreanName;
        this.englishName = englishName;
        this.categoryDepth = categoryDepth;
        this.parent = parent;
    }
}