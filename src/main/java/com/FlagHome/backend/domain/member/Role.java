package com.FlagHome.backend.domain.member;

import com.FlagHome.backend.domain.auth.JoinType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ROLE_USER, ROLE_CLUB, ROLE_ADMIN
    ;

    /**
     * JoinTpye에 맞는 권한을 리턴 (Casting이 일어날 일이 없어 NPE가 발생 X - 삼항 연산자로 구현)
     * @param joinType
     * @return 유저 권한
     */
    public static Role getRole(JoinType joinType) {
        return joinType == JoinType.CLUB ? ROLE_CLUB : ROLE_USER;
    }
}