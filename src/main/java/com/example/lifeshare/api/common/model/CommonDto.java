package com.example.lifeshare.api.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 공통코드 정보 DTO
 *
 * @author ChoiJisoo
 * @version 1.0
 **/
@Schema(description = "공통코드 정보")
@Data
public class CommonDto {

    /**
     * 대분류코드
     */
    @Schema(description = "codeType")
    private String codeType;

    /**
     * 중분류코드
     */
    @Schema(description = "grpType")
    private String grpType;

    /**
     * 코드아이디
     */
    @Schema(description = "codeId")
    private String codeId;

    /**
     * 코드명
     */
    @Schema(description = "codeNm")
    private String codeNm;

    /**
     * 사용여부
     */
    @Schema(description = "useYn")
    private String useYn;

    /**
     * 초기값 여부
     */
    @Schema(description = "defYn")
    private String defYn;
}
