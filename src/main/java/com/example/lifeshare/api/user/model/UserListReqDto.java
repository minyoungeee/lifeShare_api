package com.example.lifeshare.api.user.model;

import com.example.lifeshare.cmm.dto.ListCommonDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * @className : UserListReqDto
 * @description : 유저 목록 조회 요청 dto 클래스
 * @date : 2022-05-27 오후 3:10
 * @author : parksujin
 * @version : 1.0.0
 * @see
 * @history :
**/
@Schema(description = "유저 목록 조회 요청")
@Data
@SuperBuilder
public class UserListReqDto extends ListCommonDto {

    /**
     * 사용자 ID
     */
    @Schema(description = "사용자 ID", nullable = true)
    private String userId;

    /**
     * 사용자 명
     */
    @Schema(description = "사용자 명", nullable = true)
    private String userNm;

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
     * 검색어
     */
    @Schema(description = "검색어", nullable = true)
    private String search;

}
