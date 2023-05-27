package com.Flaground.backend.global.common.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class DuplicationResponse {
    private boolean exist;

    public static DuplicationResponse from(Boolean isExist) {
        return new DuplicationResponse(isExist);
    }
}
