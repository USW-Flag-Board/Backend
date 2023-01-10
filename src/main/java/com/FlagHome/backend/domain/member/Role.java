package com.FlagHome.backend.domain.member;

import com.FlagHome.backend.domain.auth.JoinType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ROLE_USER, ROLE_CREW, ROLE_ADMIN
    ;

    /**
     * JoinTpye에 맞는 권한을 리턴
     * @param joinType
     * @return 유저 권한
     */
    public static Role from(JoinType joinType) {
        if (joinType == JoinType.동아리) {
            return ROLE_CREW;
        }
        return ROLE_USER;
    }
}