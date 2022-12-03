package com.FlagHome.backend.v1.log.entity;

import com.FlagHome.backend.v1.BaseEntity;
import com.FlagHome.backend.v1.post.entity.Post;
import com.FlagHome.backend.v1.member.entity.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
