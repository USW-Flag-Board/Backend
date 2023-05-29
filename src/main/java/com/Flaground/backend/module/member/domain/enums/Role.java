package com.Flaground.backend.module.member.domain.enums;

import com.Flaground.backend.module.auth.domain.JoinType;
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
        return joinType == JoinType.CREW ? ROLE_CREW : ROLE_USER;
    }
}
