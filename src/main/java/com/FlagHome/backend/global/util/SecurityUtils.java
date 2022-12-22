package com.FlagHome.backend.global.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtils {

    private SecurityUtils() {};

    /**
     * SecurityContextHolder에 저장된 인증 정보로부터 memberId를 가져온다.
     * 즉, 가져올 수 있는 경우는 JWT 필터로 인증이 완료된 대상을 말한다.
     * @return (Long) memberId
     */
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
