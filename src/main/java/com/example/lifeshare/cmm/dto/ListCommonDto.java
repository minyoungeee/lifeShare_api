package com.example.lifeshare.cmm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * @className : ListCommonDto
 * @description : 목록정보 페이징 설정 dto 클래스
 * @date : 2022-05-26 오후 3:58
 * @author : parksujin
 * @version : 1.0.0
 * @see
 * @history :
**/
@Schema(description = "목록정보 페이징 설정 dto 클래스")
@Data
@SuperBuilder
public class ListCommonDto {

	/**
     * 가져올 row 갯수
     */
	@Schema(description = "가져올 row 갯수", nullable = true)
	private int limit = 0;

	/**
     * 페이지 번호
     */
	@Schema(description = "페이지 번호", nullable = true)
	private int pageNo = 0;

	/**
     * 시작 인덱스
     */
	@Schema(description = "시작 인덱스", nullable = true)
	private int startIdx = 0;

	/**
     * order by 항목
     */
	@Schema(description = "order by 항목", nullable = true)
	private int order = 0;

	public int getStartIdx(){
		return this.pageNo;//this.pageNo != 0 ? this.limit * (this.pageNo-1) : 0;
	}

}
