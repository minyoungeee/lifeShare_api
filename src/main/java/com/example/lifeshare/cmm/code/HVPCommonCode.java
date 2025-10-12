/*
 * Roadeyes version 1.0
 *
 *  Copyright ⓒ 2022 kt corp. All rights reserved.
 *
 *  This is a proprietary software of kt corp, and you may not use this file except in
 *  compliance with license agreement with kt corp. Any redistribution or use of this
 *  software, with or without modification shall be strictly prohibited without prior written
 *  approval of kt corp, and the copyright notice above does not evidence any actual or
 *  intended publication of such software.
 */
package com.example.lifeshare.cmm.code;

import lombok.Getter;

/**
 * @className : HVPCommonCode
 * @description : 사용자 권한 ENUM 클래스
 * @autor : parksujin
 * @date : 2022-05-24 오후 3:24
 */
public enum HVPCommonCode {

	USER_GRAD_ADMIN("U00", "관리자"),
	USER_GRAD_USER("U01", "사용자")
	;

	/**
     * 코드
     */
	@Getter
	private final String code;

	/**
     * 이름
     */
	@Getter
	private final String name;

	/**
     * @name : HVPCommonCode
     * @description : HVPCommonCode 생성자
     * - 코드 및 이름 정보 초기화
     * @param code : 코드정보
     * @param name : 이름 정보
     */
	private HVPCommonCode(String code, String name) {
		this.code = code;
		this.name = name;
	}

}
