package com.hufs_cheongwon.domain;

public enum Category {
    ALL("전체"),
    EDUCATION("교육")
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
