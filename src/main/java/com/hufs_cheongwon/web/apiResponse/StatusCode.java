package com.hufs_cheongwon.web.apiResponse;

public enum StatusCode {

    COMMON("COMMON"),
    USER("USER"),
    ADMIN("ADMIN"),
    CATEGORY("CATEGORY"),
    EMAIL("EMAIL"),
    PETITION("PETITION"),
    ANSWER("ANSWER"),
    BOARD("BOARD"),
    ;

    private final String prefix;

    StatusCode(String prefix){
        this.prefix = prefix;
    }

    public String getCode(int codeNumber){
        return this.prefix + codeNumber;
    }
}
