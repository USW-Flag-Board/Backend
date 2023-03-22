package com.FlagHome.backend.domain.member.avatar.entity;


import com.FlagHome.backend.domain.member.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Avatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    private String nickName;

    @Column
    private String bio;

    @Column(name = "profile_img")
    private String profileImage;

    public void updateAvatar(Avatar avatar) {
        this.nickName = avatar.getNickName();
        this.bio = avatar.getBio();
    }

    public void changeProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    @Builder
    public Avatar(Member member, String nickName, String bio, String profileImage) {
        this.member = member;
        this.nickName = nickName;
        this.bio = bio;
        this.profileImage = profileImage;
    }

    public static Avatar of(Member member, String nickName) {
        return Avatar.builder()
                .member(member)
                .nickName(nickName)
                .bio("")
                .profileImage("default")
                .build();
    }
}
