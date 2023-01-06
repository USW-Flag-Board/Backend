package com.FlagHome.backend.domain.activity.entity;

import com.FlagHome.backend.domain.activity.Status;
import com.FlagHome.backend.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id")
    private Member leader;

    // OneToMany의 떨어지는 성능에 더 고려필요.
//    private List<Member> members;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;
}
