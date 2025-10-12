package com.example.lifeshare.api.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @className : UserDto
 * @description : 유저정보 dto 클래스
 * @date : 2022-05-02 오후 1:04
 * @author : parksujin
 * @version : 1.0.0
 * @see
 * @history :
**/
@Schema(description = "유저 정보")
@Data
public class UserDto {

    /**
     * 사용자 ID
     */
    @Schema(description = "사용자 ID", nullable = true)
    private String adminId;

    /**
     * 패스워드
     */
    @Schema(description = "패스워드", nullable = true)
    private String adminPw;

    /**
     * 새 패스워드
     */
    @Schema(description = "새 패스워드", nullable = true)
    private String newPwd;

    /**
     * 사용자 명
     */
    @Schema(description = "사용자 명", nullable = true)
    private String adminNm;

    /**
     * 이메일
     */
    @Schema(description = "이메일", nullable = true)
    private String email;

    /**
     * 사용 여부
     */
    @Schema(description = "사용 여부", nullable = false)
    private String useYn;

    /**
     * 비밀번호 초기화 상태 요청
     */
    @Schema(description = "비밀번호 초기화 상태 요청", nullable = false)
    private String pwdResetSttus;

    /**
     * 마지막 비밀번호 갱신 일시
     */
    @Schema(description = "마지막 비밀번호 갱신 일시", nullable = true)
    private String pwdResetDt;

    /**
     * 최근 접속 일시
     */
    @Schema(description = "최근 접속 일시", nullable = true)
    private String loginDt;

    /**
     * 등록 일시
     */
    @Schema(description = "등록 일시", nullable = true)
    private String registDt;

    /**
     * 수정 아이디
     */
    @Schema(description = "수정 아이디", nullable = true)
    private String updtId;

    /**
     * 최근 접속 일시
     */
    @Schema(description = "최근 접속 일시", nullable = true)
    private String latlyLoginDt;

}
