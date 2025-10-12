package com.example.lifeshare.api.auth.model;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 인증 DTO 클래스
 *
 * @author chauki
 * @version 1.0
 **/
@Data
public class AuthDto {

    /**
     * 유저 아이디
     */
    @NotBlank(message = "userId는 필수 입력값입니다.")
    private String userId;

    /**
     * 유저 비밀번호
     */
    @NotBlank(message = "pwd는 필수 입력값입니다.")
    private String pwd;
}
