package com.example.lifeshare.cmm.jwt;

import com.example.lifeshare.api.user.model.UserDto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final CookieUtil cookieUtil;

    @Autowired
    public JwtRequestFilter(JwtUserDetailsService jwtUserDetailsService,
                            JwtTokenUtil jwtTokenUtil,
                            CookieUtil cookieUtil) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.cookieUtil = cookieUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String token = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            token = requestTokenHeader.substring(7).trim();

            // 빠른 형식 검사: JWT는 정확히 '.'이 2개 있어야 함
            long dotCount = (token == null) ? 0 : token.chars().filter(ch -> ch == '.').count();
            if (token == null || token.isEmpty() || dotCount != 2) {
                // 개발 시 디버깅용 로그. 프로덕션에선 토큰 값은 로그에 남기지 마세요.
                log.debug("Skipping JWT parse because token format invalid (dotCount={})", dotCount);
                request.setAttribute("invalid-jwt-format", true);
            } else {
                try {
                    username = jwtTokenUtil.getUsernameFromToken(token);
                } catch (ExpiredJwtException e) {
                    log.info("JWT Token has expired");
                    request.setAttribute("token-expired", true);

                    // Refresh 흐름: 만료된 access 토큰이 있을 때만 시도
                    Cookie refreshToken = cookieUtil.getCookie(request, jwtTokenUtil.REFRESH_TOKEN_NAME);
                    if (refreshToken != null) {
                        String refreshJwt = refreshToken.getValue();
                        try {
                            String refreshUname = jwtTokenUtil.getUsernameFromToken(refreshJwt);
                            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(refreshUname);

                            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                                    new UsernamePasswordAuthenticationToken(
                                            userDetails, null, userDetails.getAuthorities());
                            usernamePasswordAuthenticationToken.setDetails(
                                    new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                            // Access & Refresh 재발급
                            UserDto user = new UserDto();
                            user.setAdminId(refreshUname);
                            String newToken = jwtTokenUtil.generateToken(user);
                            String newRefreshJwt = jwtTokenUtil.generateRefreshToken(user);

                            jakarta.servlet.http.Cookie newRefreshToken = cookieUtil.createCookie(
                                    JwtTokenUtil.REFRESH_TOKEN_NAME,
                                    newRefreshJwt,
                                    (int) JwtTokenUtil.REFRESH_TOKEN_VALIDATION_SECOND
                            );

                            response.addHeader(AuthConstants.AUTH_HEADER,
                                    AuthConstants.TOKEN_TYPE + " " + newToken);
                            response.addCookie(newRefreshToken);
                        } catch (JwtException ex) {
                            log.warn("Refresh token invalid or expired, cannot re-issue access token.");
                            request.setAttribute("invalid-refresh-token", true);
                        } catch (Exception ex) {
                            log.error("Unexpected error while handling refresh token", ex);
                        }
                    }
                } catch (JwtException e) {
                    // MalformedJwtException 포함 모든 JwtException 처리
                    log.warn("Invalid JWT token, authentication will be skipped: {}", e.getMessage());
                    request.setAttribute("invalid-jwt", true);
                } catch (IllegalArgumentException e) {
                    log.warn("Unable to get JWT Token: {}", e.getMessage());
                    request.setAttribute("invalid-jwt", true);
                } catch (Exception e) {
                    log.error("Unexpected error while parsing JWT", e);
                    request.setAttribute("invalid-jwt", true);
                }
            }
        } else {
            // Authorization 헤더가 아예 없거나 Bearer 가 아니면 DEBUG로 남김 (매 요청마다 WARN 찍지 않음)
            log.debug("No Bearer token present in Authorization header");
        }

        // 유저 인증 처리: username이 정상일 때만 시도
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
                if (jwtTokenUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            } catch (Exception e) {
                log.warn("Failed to authenticate user from JWT: {}", e.getMessage());
            }
        }

        chain.doFilter(request, response);
    }
}
