package com.example.lifeshare.cmm.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @className : JwtAccessDeniedHandler
 * @description : JWT 인증 시 권한 부족(403 Forbidden) 예외 처리ㄴ
 * @date : 2021-04-05
 * @version : 1.0.0
 */
@Component
@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

   @Override
   public void handle(HttpServletRequest request,
                      HttpServletResponse response,
                      AccessDeniedException accessDeniedException)
           throws IOException, ServletException {

      log.warn("Access denied: {}", accessDeniedException.getMessage());

      Map<String, Object> resultMap = new HashMap<>();
      resultMap.put("code", HttpServletResponse.SC_FORBIDDEN);
      resultMap.put("msg", "Access Denied: " + accessDeniedException.getMessage());
      resultMap.put("serverTime", System.currentTimeMillis());

      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      response.setContentType("application/json;charset=UTF-8");

      ObjectMapper mapper = new ObjectMapper();
      mapper.writeValue(response.getWriter(), resultMap);
   }
}
