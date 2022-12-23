package com.FlagHome.backend.domain.log.entity;

import com.FlagHome.backend.domain.BaseEntity;
import com.FlagHome.backend.domain.Status;
import com.FlagHome.backend.domain.member.entity.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
}
