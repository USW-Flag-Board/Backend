package com.Flaground.backend.global.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RandomGenerator {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int LENGTH = 6;
    private static final int RANDOM_START = 48;
    private static final int RANDOM_END = 58;

    public static String getRandomNumber() {
        return RANDOM.ints(RANDOM_START, RANDOM_END)
                .limit(LENGTH)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint,
                        StringBuilder::append)
                .toString();
    }
}
