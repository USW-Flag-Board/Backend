package com.FlagHome.backend.domain.member.avatar.entity;


import com.FlagHome.backend.domain.member.avatar.dto.UpdateAvatarRequest;
import com.FlagHome.backend.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
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
    private String profileImg;

    public void updateAvatar(UpdateAvatarRequest updateAvatarRequest) {
        this.nickName = updateAvatarRequest.getNickName();
        this.bio = updateAvatarRequest.getBio();
        this.profileImg = updateAvatarRequest.getProfileImg();
    }

    public static Avatar of(Member member, String nickName) {
        return Avatar.builder()
                .member(member)
                .nickName(nickName)
                .bio("")
                .profileImg("default")
                .build();
    }
}
