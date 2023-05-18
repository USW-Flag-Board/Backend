package com.Flaground.backend.module.auth.controller.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class DuplicationResponse {
    private boolean exist;

    public static DuplicationResponse from(Boolean isExist) {
        return new DuplicationResponse(isExist);
    }
}
