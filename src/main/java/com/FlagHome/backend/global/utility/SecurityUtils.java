package com.FlagHome.backend.global.utility;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    private SecurityUtils() {};

    public static Long getMemberId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (hasMemberId(authentication)) {
            return Long.parseLong(authentication.getName());
        }

        return null;
    }

    private static boolean hasMemberId(Authentication authentication) {
        return authentication != null && authentication.getName() != null;
    }
}
