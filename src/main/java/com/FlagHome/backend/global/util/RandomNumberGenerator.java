package com.FlagHome.backend.global.util;

import java.security.SecureRandom;

public class RandomNumberGenerator {
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * 이메일 인증에 필요한 6자리 숫자(난수) 생성
     * @return (String) 6자리 인증번호
     */
    public static String getRandomNumber() {
        int length = 6;
        int randomNumberStart = 48;
        int randomNumberEnd = 57;

        return RANDOM.ints(randomNumberStart, randomNumberEnd + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint,
                        StringBuilder::append)
                .toString();
    }

    /**
     * 비밀번호 재발급에 사용되는 10자리 숫자 + 알파벳(대소문자) 생성
     * @return (String) 10자리 비밀번호
     */
    public static String getRandomPassword() {
        int length = 10;
        int randomNumberStart = 48;
        int randomNumberEnd = 122;

        return RANDOM.ints(randomNumberStart, randomNumberEnd + 1)
                    .filter(i -> Character.isAlphabetic(i) || Character.isDigit(i))
                    .limit(length)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint,
                            StringBuilder::append)
                    .toString();
    }
}
