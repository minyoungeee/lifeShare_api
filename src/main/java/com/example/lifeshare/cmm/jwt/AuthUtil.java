package com.example.lifeshare.cmm.jwt;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @className : AuthUtil
 * @description : AuthUtil 클래스
 * @date : 2022-05-20 오전 11:01
 * @author : parksujin
 * @version : 1.0.0
 * @see
 * @history :
**/
@Component
public class AuthUtil {

    /**
     * JwtTokenUtil 객체
     */
    private final JwtTokenUtil jwtTokenUtil;

    /**
     * CookieUtil 객체
     */
    private final CookieUtil cookieUtil;

    /**
    * @funcName : AuthUtil
    * @description :
    * @param jwtTokenUtil : JwtTokenUtil 객체
    * @param cookieUtil : CookieUtil 객체
    * @return :
    * @exception :
    * @date : 2022-05-20 오전 11:01
    * @author : parksujin
    * @see
    * @history :
    **/
    public AuthUtil(JwtTokenUtil jwtTokenUtil,
                    CookieUtil cookieUtil) {
        this.cookieUtil = cookieUtil;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /**
    * @funcName : getUsername
    * @description : 헤더의 jwt에서 유저이름을 가져온다.
    * @param request : HttpServletRequest 객체
    * @param response : HttpServletResponse 객체
    * @return String : userId
    * @exception :
    * @date : 2022-05-20 오전 11:01
    * @author : parksujin
    * @see
    * @history :
    **/
    public String getUsername(HttpServletRequest request, HttpServletResponse response) {
        final String requestTokenHeader = request.getHeader("Authorization");
        final String responseTokenHeader = response.getHeader("Authorization");
        if (responseTokenHeader != null && responseTokenHeader.startsWith("Bearer ")) {
            String jwtToken = responseTokenHeader.substring(7);
            if (jwtToken != null) {
                return jwtTokenUtil.getUsernameFromToken(jwtToken);
            }
        }else {
            if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
                String jwtToken = requestTokenHeader.substring(7);
                if (jwtToken != null) {
                    return jwtTokenUtil.getUsernameFromToken(jwtToken);
                }
            }
        }
        return null;
    }
}
