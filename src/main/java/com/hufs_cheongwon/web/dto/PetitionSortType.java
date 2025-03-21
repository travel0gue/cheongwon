package com.hufs_cheongwon.web.dto;

public enum PetitionSortType {
    CREATED_AT("createdAt"),
    AGREE_COUNT("agreeCount");

    private final String fieldName;

    PetitionSortType(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
