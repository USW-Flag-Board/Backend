package com.FlagHome.backend.domain.activity.entity;

import com.FlagHome.backend.domain.activity.Proceed;
import com.FlagHome.backend.domain.activity.Status;
import com.FlagHome.backend.domain.activity.Type;
import com.FlagHome.backend.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column
    @Enumerated(EnumType.STRING)
    private Proceed proceed;

    @OneToOne
    private Member leader;

    @OneToMany
    private List<Member> members;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String period;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
}
