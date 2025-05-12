package com.hufs_cheongwon.domain.enums;

public enum Category {
    ACADEMIC("학사/교육"),
    WELFARE("행정/복지"),
    IT_SERVICE("IT/정보 서비스"),
    ACTIVITIES("학생 활동"),
    CAREER("취업/진로"),
    GUITAR("기타")
    ;

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    // 화면에 표시되는 이름으로 enum 찾기
    public static Category fromDisplayName(String displayName) {
        for (Category type : values()) {
            if (type.getDisplayName().equals(displayName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown category display name: " + displayName);
    }

    // enum 이름을 소문자로 변환 (URL 경로나 API 요청에서 사용)
    public String toLowerCaseString() {
        return this.name().toLowerCase();
    }
}
