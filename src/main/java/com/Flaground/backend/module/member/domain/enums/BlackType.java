package com.Flaground.backend.module.member.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BlackType {
    BAN(3),
    SUSPEND(9999);

    private final int blackPeriod;

    private int getPeriod() {
        return this.blackPeriod;
    }
}
