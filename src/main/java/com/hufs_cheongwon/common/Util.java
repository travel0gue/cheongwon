package com.hufs_cheongwon.common;

public class Util {
    /**
     * 이메일 마스킹 유틸: abcde@domain.com → a***@d***
     */
    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) return "invalid";
        String[] parts = email.split("@");
        return parts[0].charAt(0) + "***@" + parts[1].charAt(0) + "***";
    }
}
