package com.example.lifeshare.cmm.jwt;

import com.example.lifeshare.api.user.model.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @className : JwtTokenUtil
 * @description : jwt 토큰 utill 클래스
 * - 토큰 유효성 체크 및 토큰 생성
 * @date : 2021-04-05
 * @author : parksujin
 * @version : 1.0.0
 * @see
 * @history :
 **/
@Component
public final class JwtTokenUtil implements Serializable {

    /**
     * 직렬 버전 UID
     */
    private static final long serialVersionUID = -2550185165626007488L;

    /**
     * access 토큰 만료기한
     */
    public final static long TOKEN_VALIDATION_SECOND = 60 * 15; //15분
   // public final static long TOKEN_VALIDATION_SECOND = 60 * 60 * 24 * 7 * 100 ; //일주일

    /**
     * refresh 토큰 만료기한
     */
    public final static long REFRESH_TOKEN_VALIDATION_SECOND = 60 * 60 * 24 * 365 * 1000 ; //일주일

    /**
     * refresh 토큰 이름
     */
    public final static String REFRESH_TOKEN_NAME = "refreshToken";

    /**
     * 비밀 키
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * @name getUsernameFromToken
     * @description 토큰으로부터 sub 추출
     * @param token : token 값
     * @return 아이디(sub에 저장한 값)
     * @author : parksujin
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * @name getExpirationDateFromToken
     * @description 토큰의 만료기한 확인
     * @param token : token 값
     * @return 만료기한
     * @author : parksujin
     */
    public  Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * @name getClaimFromToken
     * @description 토큰으로 부터 Claim의 값을 추출
     * - 파라미터 정보
     *  1. token : token 값
     *  2. claimsResolver : Claims::getSubject
     * @return 아이디(sub에 저장한 값)
     * @author : parksujin
     */
    public  <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * @name getAllClaimsFromToken
     * @description 토큰으로 부터 Claim 모든 값 추출
     * @param token : token값
     * @return Claims의 body
     * @author : parksujin
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * @name isTokenExpired
     * @description 토큰이 만료되어었는지 확인
     * @param token : token값
     * @return boolean : 토큰 기한 만료 유무
     * @author : parksujin
     */
    private  Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * @name generateToken
     * @description 토큰 생성
     * @param user : UserDto
     * @return access 토큰
     * @author : parksujin
     */
    public String generateToken(UserDto user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getAdminId());
        return doGenerateToken(claims, user.getAdminId(), TOKEN_VALIDATION_SECOND);
    }

    /**
     * @name generateRefreshToken
     * @description refresh 토큰 생성
     * @param user : UserDto
     * @return refresh 토큰
     * @author : parksujin
     */
    public String generateRefreshToken(UserDto user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getAdminId());
        return doGenerateToken(claims, user.getAdminId(), REFRESH_TOKEN_VALIDATION_SECOND);
    }

    /**
     * @name doGenerateToken
     * @description tokens 생성
     * - 파라미터 정보
     *  1. claims : claims 정보
     *  2. subject : 아이디
     *  3. expireTime : 만료 기한
     * @return tokens
     * @author : parksujin
     */
    private  String doGenerateToken(Map<String, Object> claims, String subject, long expireTime) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTime * 1000L))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    /**
     * @name validateToken
     * @description 토큰과 유저 정보로 토큰의 유효성을 검증한다
     * - 파라미터 정보
     *  1. token : 토큰 값
     *  2. userDetails : 유저 정보
     * @return boolean : 토큰 유효 유무
     * @author : parksujin
     */
    public  Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
