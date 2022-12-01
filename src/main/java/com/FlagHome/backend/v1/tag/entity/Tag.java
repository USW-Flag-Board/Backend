package com.FlagHome.backend.v1.tag.entity;

import com.FlagHome.backend.v1.BaseEntity;
import com.FlagHome.backend.v1.member.entity.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tag extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Member member;

    @Column
    private String description;
}
