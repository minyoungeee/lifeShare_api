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

public enum HVPReponseCode {
	SUCCESS("0000", "성공")
	, FAILED("9000", "실패")
	, FORBIDDEN("9998", "권한이 없습니다.")
	, EXCEPTION("9999", "Exception")

	/* 공통 */
	, COMMON_INVALID_PARAMETER("9001", "필수 파라미터가 없거나 값이 유효하지 않습니다.")

	/* 사용자 */
	, USER_ERROR("1001", "회원 작업 중 알 수 없는 오류가 발생하였습니다.")
	, USER_ALREADY_EXIST("1002", "중복된 사용자 ID입니다.")
	, USER_NOT_FOUND("1003", "사용자 정보를 찾을 수 없습니다.")
	, USER_NOT_MATCH("1004", "사용자 정보가 일치하지 않습니다.")

	/* 서버 */
	, SERVER_NOT_AVAILABLE("1051", "영상을 수신할 수 있는 서버를 찾을 수 없습니다.")

	, CCTV_NAME_ALREADY_EXIST("2001", "중복된 카메라 이름입니다.")

	/* vod */
	, VOD_FILE_NOT_DELETED("2051", "하위 파일 또는 디렉터리를 삭제할 수 없습니다. 파일이 열려있을 가능성이 있습니다.")

	, AREA_INFO_ALREADY_EXIST("3001", "중복된 지역명 입니다.")
	, AREA_INFO_NOT_CHANGED("3001", "변경된 데이터가 없습니다.")

	/* 분석엔진 */
	, ENGINE_ALREADY_EXIST("3051", "중복된 분석엔진 ID입니다.")

	/* local */
	, LOCAL_NOT_DELETED("4051", "해당 지역에 등록된 카메라가 존재하여 삭제할 수 없습니다.")
	;

	@Getter
	private String code;

	@Getter
	private String message;

	private HVPReponseCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

}
