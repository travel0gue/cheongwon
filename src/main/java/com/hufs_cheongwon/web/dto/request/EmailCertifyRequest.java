package com.hufs_cheongwon.web.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EmailCertifyRequest {

    @Email
    private String email;

    private String code;
}
