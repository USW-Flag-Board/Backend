package com.FlagHome.backend.domain.member.entity.enums;

import com.FlagHome.backend.domain.auth.JoinType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public enum Role {
    ROLE_USER("USER"),
    ROLE_CREW("CREW"),
    ROLE_ADMIN("ADMIN")
    ;

    private final String role;

    /**
     * JoinTpye에 맞는 권한을 리턴
     */
    public static Role from(JoinType joinType) {
        if (joinType == JoinType.동아리) {
            return ROLE_CREW;
        }
        return ROLE_USER;
    }
}