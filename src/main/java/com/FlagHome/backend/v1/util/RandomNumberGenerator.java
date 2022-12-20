package com.FlagHome.backend.v1.util;

import java.security.SecureRandom;

public class RandomNumberGenerator {
    private static String RANDOM_NUMBER;
    private static String RANDOM_PASSWORD;
    private static SecureRandom RANDOM = new SecureRandom();

    private RandomNumberGenerator() { }

    public static String getRandomNumber() {
        int length = 6;
        int randomNumberStart = 48;
        int randomNumberEnd = 57;

        RANDOM_NUMBER = RANDOM.ints(randomNumberStart, randomNumberEnd + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint,
                        StringBuilder::append)
                .toString();

        return RANDOM_NUMBER;
    }

    public static String getRandomPassword() {
        int length = 10;
        int randomNumberStart = 48;
        int randomNumberEnd = 122;

        RANDOM_PASSWORD = RANDOM.ints(randomNumberStart, randomNumberEnd + 1)
                    .filter(i -> Character.isAlphabetic(i) || Character.isDigit(i))
                    .limit(length)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint,
                            StringBuilder::append)
                    .toString();

        return RANDOM_PASSWORD;
    }
}
