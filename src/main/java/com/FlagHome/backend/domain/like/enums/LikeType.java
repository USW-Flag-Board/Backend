package com.FlagHome.backend.domain.like.enums;

import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;

public enum LikeType {
    POST("POST"),
    REPLY("REPLY");

    private final String type;

    LikeType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type;
    }

    public static LikeType of(String typeString) {
        if(typeString.equals(POST.type))
            return LikeType.POST;
        else if(typeString.equals(REPLY.type))
            return LikeType.REPLY;
        else
            throw new CustomException(ErrorCode.NOT_SUPPORT_LIKE);
    }
}
