package com.example.lifeshare.cmm.jwt;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @className : AuthConstants
 * @description : header 설정 상수 클래스
 * @date : 2021-04-05
 * @author : parksujin
 * @version : 1.0.0
 * @see
 * @history :
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthConstants {

    /**
     * header key 이름
     */
    public static final String AUTH_HEADER = "Authorization";

    /**
     * header value 타입
     */
    public static final String TOKEN_TYPE = "Bearer";

}