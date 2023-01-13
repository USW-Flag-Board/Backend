package com.FlagHome.backend.domain.activity.entity;

import com.FlagHome.backend.domain.BaseEntity;
import com.FlagHome.backend.domain.activity.Proceed;
import com.FlagHome.backend.domain.activity.Status;
import com.FlagHome.backend.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "activity_type")
public abstract class Activity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id")
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member leader;

    @Column
    @Enumerated(EnumType.STRING)
    private Proceed proceed;

    @Column
    private String githubLink;

    @Column
    private Boolean isBookExist;

    @Column
    private String bookName;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;
}