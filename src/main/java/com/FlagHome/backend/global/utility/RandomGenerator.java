package com.FlagHome.backend.global.utility;

import java.security.SecureRandom;

public class RandomGenerator {
    private static final SecureRandom RANDOM = new SecureRandom();

    private RandomGenerator() { }

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
}
