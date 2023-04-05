package com.FlagHome.backend.domain.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
public enum JoinType {
    일반("NORMAL"),
    동아리("CREW");

    JoinType(String type) {
        this.type = type;
    }

    private String type;
}
