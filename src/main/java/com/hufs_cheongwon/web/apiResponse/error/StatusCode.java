package com.hufs_cheongwon.web.apiResponse.error;

public enum StatusCode {

    COMMON("COMMON"),
    USER("USER"),
    ADMIN("ADMIN"),
    CATEGORY("CATEGORY"),
    EMAIL("EMAIL"),
    ;

    private final String prefix;

    StatusCode(String prefix){
        this.prefix = prefix;
    }

    public String getCode(int codeNumber){
        return this.prefix + codeNumber;
    }
}
