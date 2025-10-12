package com.example.lifeshare.cmm.jwt;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @className : CookieUtil
 * @description : Cookie 메소드 클래스
 * @date : 2021-04-05
 * @author : parksujin
 * @version : 1.0.0
 * @see
 * @history :
 **/
@Service
public class CookieUtil {

     /**
     * @name createCookie
     * @description cookie를 생성한다
     * - 파라미터 정보
     *  1. cookieName : 쿠키 명
     *  2. value : 쿠키 값
     *  3. maxAge : 만료기한
     * @return cookie
     * @author : parksujin
     */
    public Cookie createCookie(String cookieName, String value, int maxAge){
        Cookie token = new Cookie(cookieName,value);
        token.setHttpOnly(true);
        token.setMaxAge(maxAge);
        token.setPath("/");
        token.setSecure(false);
        return token;
    }

    /**
     * @name getCookie
     * @description cookie를 가져온다
     * - 파라미터 정보
     *  1. req : HttpServletRequest 인터페이스 객체
     *  2. cookieName : 쿠키 명
     * @return cookie
     * @author : parksujin
     */
    public Cookie getCookie(HttpServletRequest req, String cookieName){
        final Cookie[] cookies = req.getCookies();
        if(cookies==null) return null;
        for(Cookie cookie : cookies){
            if(cookie.getName().equals(cookieName))
                return cookie;
        }
        return null;
    }

}