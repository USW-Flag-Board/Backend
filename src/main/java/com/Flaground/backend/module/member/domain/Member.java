package com.Flaground.backend.module.member.domain;

import com.Flaground.backend.global.common.BaseEntity;
import com.Flaground.backend.global.exception.CustomException;
import com.Flaground.backend.global.exception.ErrorCode;
import com.Flaground.backend.global.exception.domain.LoginFailException;
import com.Flaground.backend.module.member.domain.BlackState.BlackState;
import com.Flaground.backend.module.member.domain.enums.MemberStatus;
import com.Flaground.backend.module.member.domain.enums.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "login_id")
    private String loginId;

    @Column
    private String password;

    @Column
    private String email;

    @Column
    private String name;

    @Embedded
    private Avatar avatar;

    @Embedded
    private IssueRecord issueRecord;

    @Enumerated(EnumType.STRING)
    @Column
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column
    private MemberStatus status;

    @Builder
    public Member(String loginId, String password, String email, String name, Avatar avatar,
                  IssueRecord issueRecord, Role role, MemberStatus status) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.name = name;
        this.avatar = avatar;
        this.issueRecord = issueRecord;
        this.role = role;
        this.status = status;
    }

    public void isLoginnable() {
        isBanned();
        isLocked();
        isWithdraw();
    }

    public void isWithdraw() {
        if (this.status == MemberStatus.WITHDRAW) {
            throw new CustomException(ErrorCode.WITHDRAW_ACCOUNT);
        }
    }

    public void validateName(String name) {
        if (!StringUtils.equals(this.name, name)) {
            throw new CustomException(ErrorCode.EMAIL_NAME_NOT_MATCH);
        }
    }

    public void validateLoginId(String loginId) {
        if (!StringUtils.equals(this.loginId, loginId)) {
            throw new CustomException(ErrorCode.EMAIL_ID_NOT_MATCH);
        }
    }

    public void validatePassword(String password, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(password, this.password)) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
        }
    }

    public void validateSamePassword(String password, PasswordEncoder passwordEncoder) {
        if (passwordEncoder.matches(password, this.password)) {
            throw new CustomException(ErrorCode.PASSWORD_IS_SAME);
        }
    }

    public void updatePassword(String password, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
        unLock();
    }

    public void changeProfileImage(String profileImage) {
        avatar.changeProfileImage(profileImage);
    }

    // todo: 기본 이미지 변경 삭제
    public String resetProfileImage() {
        return avatar.resetProfileImage();
    }

    public void updateLoginTime() {
        super.updateModifiedDate();
        issueRecord.resetLoginFailCount();
    }

    public int getFailCount() {
        return issueRecord.getLoginFailCount();
    }

    public void reactivate(String loginId, String password, String email, String name) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.name = name;
        this.status = MemberStatus.NORMAL;
    }

    public void deactivate() {
        this.status = MemberStatus.SLEEPING;
        this.loginId = null;
        this.password = null;
        this.email = null;
        this.name = null;
        this.avatar.cleanUp();
    }

    public void loginFail() {
        issueRecord.increaseFailCount();
        if (issueRecord.isMaxLoginFailCount()) {
            lock();
        }
    }

    public BlackState getState() {
        return issueRecord.blackState();
    }

    public int applyPenalty(int penalty) {
        return issueRecord.applyPenalty(penalty);
    }

    public void ban() {
        this.status = MemberStatus.BANNED;
    }

    // todo: 진짜로 삭제하기
    public void withdraw() {
        this.status = MemberStatus.WITHDRAW;
    }

    private void unLock() {
        if (this.status == MemberStatus.LOCKED) {
            this.status = MemberStatus.NORMAL;
            issueRecord.resetLoginFailCount();
        }
    }

    private void isBanned() {
        if (this.status == MemberStatus.BANNED) {
            throw new LoginFailException(ErrorCode.BANNED_ACCOUNT);
        }
    }

    private void isLocked() {
        if (this.status == MemberStatus.LOCKED) {
            throw new LoginFailException(ErrorCode.LOCKED_ACCOUNT);
        }
    }

    private void lock() {
        this.status = MemberStatus.LOCKED;
    }
}
