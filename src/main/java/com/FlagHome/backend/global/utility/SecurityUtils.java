package com.FlagHome.backend.global.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtils {
    public static Long getMemberId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return loggedIn(authentication) ? Long.parseLong(authentication.getName()) : null;
    }

    private static boolean loggedIn(Authentication authentication) {
        return authentication != null && authentication.getName() != null && !authentication.getName().equals("anonymousUser");
    }
}
