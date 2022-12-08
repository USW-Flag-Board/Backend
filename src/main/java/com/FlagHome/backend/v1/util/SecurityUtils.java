package com.FlagHome.backend.v1.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtils {

    private SecurityUtils() {};

    // SecurityContextHolder에 저장된 인증정보로부터 memberId를 가져온다.
    public static Long getMemberId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (hasMemberId(authentication)) {
            return Long.parseLong(authentication.getName());
        }

        return null;
    }

    public static boolean hasMemberId(Authentication authentication) {
        return authentication != null && authentication.getName() != null;
    }
}
