package com.FlagHome.backend.domain.member.entity;


import com.FlagHome.backend.domain.member.entity.enums.Major;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Avatar {
    @Column(length = 20)
    private String nickname;

    @Column(name = "student_id")
    private String studentId;

    @Column
    @Enumerated(EnumType.STRING)
    private Major major;

    @Column(length = 300)
    private String bio;

    @Column(name = "profile_img")
    private String profileImage;

    public void updateAvatar(Avatar avatar) {
        this.nickname = avatar.getNickname();
        this.studentId = avatar.getStudentId();
        this.major = avatar.getMajor();
        this.bio = avatar.getBio();
    }

    public void changeProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    @Builder
    public Avatar(String nickname, String studentId, Major major, String bio) {
        this.nickname = nickname;
        this.studentId = studentId;
        this.major = major;
        this.bio = bio;
        this.profileImage = "https://flaground-s3.s3.ap-northeast-2.amazonaws.com/avatar/default_image.jpg";
    }

    public static Avatar of(String nickName, String studentId, Major major) {
        return Avatar.builder()
                .nickname(nickName)
                .studentId(studentId)
                .major(major)
                .bio(" ")
                .build();
    }
    
    // 비활성화 고려하기
}
