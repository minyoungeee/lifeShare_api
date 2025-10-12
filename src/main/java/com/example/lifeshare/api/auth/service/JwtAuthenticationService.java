package com.example.lifeshare.api.auth.service;

import com.example.lifeshare.api.auth.model.AuthDto;
import com.example.lifeshare.api.auth.model.AuthDto;
import com.example.lifeshare.api.user.mapper.UserMapper;
import com.example.lifeshare.api.user.model.UserDto;
import com.example.lifeshare.api.user.service.UserService;
import com.example.lifeshare.cmm.exception.ServiceException;
import com.example.lifeshare.cmm.jwt.AuthConstants;
import com.example.lifeshare.cmm.jwt.AuthUtil;
import com.example.lifeshare.cmm.jwt.CookieUtil;
import com.example.lifeshare.cmm.jwt.JwtTokenUtil;
import com.example.lifeshare.config.AES128;
import com.example.lifeshare.config.RSA;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 인증 Service 클래스
 *
 * @author parksujin
 * @version 1.0
 **/
@Slf4j
@Service
public class JwtAuthenticationService {

    /**
     * AuthenticationManager 객체
     */
    private final AuthenticationManager authenticationManager;

    /**
     * UserService 객체
     */
    private final UserService userService;

    /**
     * JwtTokenUtil 객체
     */
    private final JwtTokenUtil jwtTokenUtil;

    /**
     * CookieUtil 객체
     */
    private final CookieUtil cookieUtil;

    /**
     * UserMapper 객체
     */
    private final UserMapper userMapper;

    /**
     * AuthUtil 객체
     */
    private final AuthUtil authUtil;

    /**
     * RSA 객체
     */
    private final RSA rsa;

    /**
     * AES128 객체
     */
    private final AES128 aes128;

    /**
     * JwtAuthenticationService 생성자
     * - Field Injection 방지
     *
     * @param authenticationManager AuthenticationManager 객체
     * @param jwtTokenUtil          JwtTokenUtil 객체
     * @param cookieUtil            CookieUtil 객체
     * @param userMapper            userMapper 객체
     * @param authUtil              authUtil 객체
     * @param aes128                AES128 객체
     * @author parksujin
     * @version 1.0
     **/
    @Autowired
    public JwtAuthenticationService(
                                    AuthenticationManager authenticationManager,
                                    JwtTokenUtil jwtTokenUtil,
                                    CookieUtil cookieUtil,
                                    UserService userService,
                                    UserMapper userMapper,
                                    AuthUtil authUtil,
                                    RSA rsa,
                                    AES128 aes128) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.cookieUtil = cookieUtil;
        this.userService = userService;
        this.userMapper = userMapper;
        this.authUtil = authUtil;
        this.rsa = rsa;
        this.aes128 = aes128;
    }

    /**
     * 로그인 인증을 수행한다.
     *
     * @param authDto  AuthDto 객체
     * @param response HttpServletResponse 객체
     * @return Map<String, Object> 로그인 정보
     * @author parksujin
     * @version 1.0
     **/
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public Map<String, Object> authLogin(@Valid AuthDto authDto,
                                         HttpServletResponse response) throws ServiceException {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            String userId = authDto.getUserId();
            String pwd = authDto.getPwd();

            log.info("로그인 요청: userId={}, pwd={}", userId, pwd);

            // 유저 정보 조회
            UserDto tmpUserDto = userService.reqGetUserInfo(userId);

            if (tmpUserDto == null) {
                log.warn("존재하지 않는 사용자: {}", userId);
                resultMap.put("login", false);
                resultMap.put("msg", "존재하지 않는 아이디입니다.");
                return resultMap;
            }

            // 비밀번호 일치 확인
            if (!pwd.equals(tmpUserDto.getAdminPw())) {
                log.warn("비밀번호 불일치: {}", userId);
                resultMap.put("login", false);
                resultMap.put("msg", "비밀번호가 일치하지 않습니다.");
                return resultMap;
            }

            // 로그인 시간 업데이트
            int updRsl = userMapper.updateUserLoginDt(userId);
            if (updRsl == 0) {
                log.warn("로그인 시간 업데이트 실패: {}", userId);
            }

            // 토큰 발급 및 로그인 성공
            resultMap = loginSuccess(tmpUserDto, response);
            resultMap.put("login", true);

            log.info("로그인 성공: {}", userId);
        } catch (Exception e) {
            log.error("로그인 처리 중 오류 발생", e);
            resultMap.put("login", false);
            resultMap.put("msg", "서버 오류가 발생했습니다. 관리자에게 문의하세요.");
        }
        return resultMap;
    }


    /**
     * 로그인 성공을 수행한다.
     *
     * @param tmpUserDto AuthDto 객체
     * @param response   HttpServletResponse 객체
     * @return Map<String, Object> 인증 결과
     * @author parksujin
     * @version 1.0
     **/
    public Map<String, Object> loginSuccess(UserDto tmpUserDto,
                                            HttpServletResponse response) throws ServiceException {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            //사용자 정보 조회 및 쿠키 설정
            final String token = jwtTokenUtil.generateToken(tmpUserDto);
            final String refreshJwt = jwtTokenUtil.generateRefreshToken(tmpUserDto);

            Cookie refreshToken = cookieUtil.createCookie(JwtTokenUtil.REFRESH_TOKEN_NAME, refreshJwt, (int) JwtTokenUtil.REFRESH_TOKEN_VALIDATION_SECOND);

            response.addHeader(AuthConstants.AUTH_HEADER, AuthConstants.TOKEN_TYPE + " " + token);
            response.addCookie(refreshToken);
//            Integer chkdate = userMapper.selectOneUserPwdResetDate(tmpUserDto.getAdminId());
            //비밀번호 변경 3개월 초과 체크
            /*if (chkdate > 90) {
                resultMap.put("pwdChange", true);
                log.info("비밀번호 변경 후 3개월 초과");
            } else {
                resultMap.put("pwdChange", false);
            }*/
            resultMap.put("login", true);
            resultMap.put("user", tmpUserDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            resultMap.put("login", false);
        }
        return resultMap;
    }

    /**
     * 로그아웃을 수행한다.
     *
     * @param request  HttpServletRequest 객체
     * @param response HttpServletResponse 객체
     * @return Boolean 로그아웃 여부
     * @author parksujin
     * @version 1.0
     **/
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public Boolean doLogout(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            // 로그아웃 시간 업데이트
            int updRsl = userMapper.updateUserLogoutDt(authUtil.getUsername(request, response));

            if (updRsl > 0) {
                Cookie refreshTokenCookie = cookieUtil.getCookie(request, JwtTokenUtil.REFRESH_TOKEN_NAME);

                if (refreshTokenCookie != null) {
                    Cookie newCookie = cookieUtil.createCookie(
                            JwtTokenUtil.REFRESH_TOKEN_NAME,
                            refreshTokenCookie.getValue(),
                            0
                    );
                    response.addCookie(newCookie);
                    log.info("Refresh token cookie cleared for user {}", authUtil.getUsername(request, response));
                } else {
                    log.warn("No refresh token cookie found for logout request. Skipping cookie clearing.");
                }
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            log.error("Logout failed", e);
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * spring security 인증을 체크한다.
     *
     * @param username 사용자계정
     * @param password 비밀번호
     * @author chauki
     * @version 1.0
     **/
//    private void authenticate(String username, String password) throws ServiceException {
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//        } catch (DisabledException e) {
//            log.error("USER_DISABLED : " + e.getMessage());
//            throw new ServiceException(e);
//        } catch (BadCredentialsException e) {
//            log.error("INVALID_CREDENTIALS : " + e.getMessage());
//            throw new ServiceException(e);
//        }
//    }

    /**
     * 공개키 정보를 조회한다.
     *
     * @return Map<String, Object> 공개키 정보
     * @author chauki
     * @version 1.0
     **/
    public Map<String, Object> getPublicKey() throws ServiceException {
        try {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("publicKey", this.rsa.getPublicKey());
            return resultMap;
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }
}
