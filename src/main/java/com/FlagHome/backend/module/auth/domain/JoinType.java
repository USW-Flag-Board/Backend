package com.FlagHome.backend.module.auth.domain;

import com.FlagHome.backend.global.common.CustomEnumDeserializer;
import com.FlagHome.backend.module.member.domain.enums.Role;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;

@Getter
@JsonDeserialize(using = CustomEnumDeserializer.class)
public enum JoinType {
    NORMAL("일반"),
    CREW("동아리");

    JoinType(String type) {
        this.type = type;
    }

    private String type;

    public Role toRole() {
        return Role.from(this);
    }
}
