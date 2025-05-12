package com.hufs_cheongwon.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UsersStatus {

    ACTIVE("활성화"),
    INACTIVE("비활성화"),
    DELETED("삭제"),
    EXPIRED("인증 만료")
    ;

    private final String description;
}
