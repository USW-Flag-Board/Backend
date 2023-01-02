package com.FlagHome.backend.domain.activity.entity;

import com.FlagHome.backend.domain.activity.Status;
import com.FlagHome.backend.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;
    private String description;
    private String period;
    private String etc;

    @OneToOne(mappedBy = "member")
    private Member leader;

    @OneToMany(mappedBy = "member")
    private List<Member> members;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;
}
