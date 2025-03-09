package com.hufs_cheongwon.web.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class TokenDto {
    private String refreshToken;
    private String accessToken;
}
