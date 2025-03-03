package com.hufs_cheongwon.web.apiResponse.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private Boolean isSuccess;
    private String code;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> validation;

    public void setValidation(Map<String, String> validation) {
        this.validation = validation;
    }
}
