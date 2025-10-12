package com.example.lifeshare.cmm.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 * @className : CookieAttributeFilter
 * @description : Cookie 설정 필터 클래스
 * @date : 2021-04-05
 * @author : parksujin
 * @version : 1.0.0
 * @see
 * @history :
 **/
@Slf4j
@WebFilter(urlPatterns= "/*")
public class CookieAttributeFilter implements Filter {

    /**
     * @name : doFilter
     * @description : 응답 전에 쿠키의 속성을 설정한다
     * - 파라미터 정보
     *  1. request ServletRequest 객체
     *  2. response ServletResponse 객체
     *  3. FilterChain FilterChain 객체
     * @throws IOException : 예외,
     *         ServletException : 예외
     * @author : parksujin
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        chain.doFilter(request, response);
        log.info("CookieAttributeFilter");
        addSameSite(httpServletResponse , "None");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

     /**
     * @name : addSameSite
     * @description : SameSite를 추가한다
     * - 파라미터 정보
     *  1. response : ServletResponse 인터페이스 객체
     *  2. sameSite : sameSite 설정 값
     * @author : parksujin
     */
    private void addSameSite(HttpServletResponse response, String sameSite) {
        Collection<String> headers = response.getHeaders(HttpHeaders.SET_COOKIE);
        boolean firstHeader = true;

        for (String header : headers) {
             if (firstHeader) {
                response.setHeader(HttpHeaders.SET_COOKIE, String.format("%s; Secure; %s", header, "SameSite=" + sameSite));
                firstHeader = false;
                continue;
            }
            response.addHeader(HttpHeaders.SET_COOKIE, String.format("%s; Secure; %s", header, "SameSite=" + sameSite));
        }
    }
}






