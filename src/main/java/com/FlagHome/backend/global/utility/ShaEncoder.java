package com.FlagHome.backend.global.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ShaEncoder {
    public enum AlgorithmType {
        SHA256("SHA-256"), SHA512("SHA-512");

        private final String algorithm;

        AlgorithmType(String algorithm) {
            this.algorithm = algorithm;
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for(byte eachByte : bytes)
            stringBuilder.append(String.format("%02x", eachByte));
        return stringBuilder.toString();
    }

    public String encode(String plainText, AlgorithmType algorithmType) {
        String digest = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithmType.algorithm);
            messageDigest.update(plainText.getBytes());
            digest = bytesToHex(messageDigest.digest());
        }
        catch(NoSuchAlgorithmException e) {
            System.out.println("SHA 암호화에 실패하였습니다. " + e.getMessage()); // logger로 대체 해야합니다.
        }

        return digest;
    }
}
