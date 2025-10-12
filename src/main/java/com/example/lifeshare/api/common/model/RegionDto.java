package com.example.lifeshare.api.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * 지역코드 정보 DTO
 *
 * @author ChoiJisoo
 * @version 1.0
 **/
@Schema(description = "지역코드 정보")
@Data
public class RegionDto {

    /**
     * 지역코드
     */
    @Schema(description = "admCode")
    private String admCode;

    /**
     * 지역명
     */
    @Schema(description = "admNm")
    private String admNm;

    /**
     * 풀 지역명
     */
    @Schema(description = "fulAdmNm")
    private String fulAdmNm;

    /**
     * 상위지역코드
     */
    @Schema(description = "uppAdmCode")
    private String uppAdmCode;

    /**
     * 레벨 (1: 시도, 2: 시군구, 3:읍면동)
     */
    @Schema(description = "lvl")
    private int lvl;

    /**
     * 중심좌표-위도
     */
    @Schema(description = "lat")
    private String lat;

    /**
     * 중심좌표-경도
     */
    @Schema(description = "lng")
    private String lng;
}
