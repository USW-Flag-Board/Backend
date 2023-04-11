package com.FlagHome.backend.domain.member.entity.enums;

import com.FlagHome.backend.domain.auth.JoinType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    ROLE_USER("USER"),
    ROLE_CREW("CREW"),
    ROLE_ADMIN("ADMIN")
    ;

    private final String role;

    public static Role from(JoinType joinType) {
        if (joinType == JoinType.CREW) {
            return ROLE_CREW;
        }
        return ROLE_USER;
    }
}