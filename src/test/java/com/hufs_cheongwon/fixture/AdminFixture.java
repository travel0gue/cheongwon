package com.hufs_cheongwon.fixture;

import com.hufs_cheongwon.domain.Admin;

public class AdminFixture {

    public static Admin createTestAdmin() {
        return Admin.builder()
                .departure("학사과")
                .role("담당자")
                .name("관리자")
                .email("admin@hufs.ac.kr")
                .password("encodedAdminPassword123")
                .phoneNumber("02-1234-5678")
                .build();
    }

    public static Admin createTestAdmin(String departure, String name, String email) {
        return Admin.builder()
                .departure(departure)
                .role("담당자")
                .name(name)
                .email(email)
                .password("encodedAdminPassword123")
                .phoneNumber("02-1234-5678")
                .build();
    }

    public static Admin createSuperAdmin() {
        return Admin.builder()
                .departure("전산실")
                .role("슈퍼관리자")
                .name("슈퍼관리자")
                .email("superadmin@hufs.ac.kr")
                .password("encodedSuperAdminPassword123")
                .phoneNumber("02-1234-5679")
                .build();
    }
}