package com.example.lifeshare.api.auth.controller;

import com.example.lifeshare.api.auth.model.AuthDto;
import com.example.lifeshare.api.auth.service.JwtAuthenticationService;
import com.example.lifeshare.cmm.annotation.ResponseWrapper;
import com.example.lifeshare.cmm.exception.ServiceException;
import com.example.lifeshare.cmm.jwt.CookieUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Map;

/**
 * JWT 인증 컨트롤러 클래스
 *
 * @author chauki
 * @version 1.0
 **/
@Tag(name = "인증 API")
@RequestMapping("/auth")
@RestController
@CrossOrigin
public class JwtAuthenticationController {

    /**
     * JwtAuthenticationService 객체
     */
    private final JwtAuthenticationService jwtAuthenticationService;

    /**
     * CookieUtil 객체
     */
    private final CookieUtil cookieUtil;

    /**
     * JwtAuthenticationController 생성자
     * - Field Injection 방지
     *
     * @param jwtAuthenticationService JwtAuthenticationService 객체
     * @param cookieUtil CookieUtil 객체
     * @author chauki
     * @version 1.0
     **/
    @Autowired
    public JwtAuthenticationController(JwtAuthenticationService jwtAuthenticationService,
                                       CookieUtil cookieUtil) {
        this.jwtAuthenticationService = jwtAuthenticationService;
        this.cookieUtil = cookieUtil;
    }

    /**
     * 로그인을 수행한다.
     *
     * @param authDto 인증 정보
     * @author chauki
     * @version 1.0
     **/
    @Operation(summary = "로그인 API (API-ATH-001)", description = "로그인을 수행한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(description = "Bad Request", example = "Bad Request "))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(description = "Unauthorized", example = "Unauthorized"))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(description = "Not Found", example = "Not Found"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(description = "Internal Server Error", example = "Internal Server Error")))
    })
    @PostMapping("/login")
    @ResponseWrapper
    public Map<String, Object> doLogin(@Parameter(name = "authDto", description = "인증정보") @RequestBody @Valid AuthDto authDto,
                                       HttpServletResponse response) throws ServiceException {
        return jwtAuthenticationService.authLogin(authDto, response);
    }

    /**
     * 로그아웃을 수행한다.
     *
     * @param request HttpServletRequest 객체
     * @param response HttpServletResponse 객체
     * @author chauki
     * @version 1.0
     **/
    @Operation(summary = "로그아웃 API (API-ATH-002)", description = "로그아웃을 수행한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(description = "Bad Request", example = "Bad Request "))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(description = "Unauthorized", example = "Unauthorized"))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(description = "Not Found", example = "Not Found"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(description = "Internal Server Error", example = "Internal Server Error")))
    })
    @PostMapping("/logout")
    @ResponseWrapper
    public Boolean doLogout(HttpServletRequest request, HttpServletResponse response) {
        return jwtAuthenticationService.doLogout(request, response);
    }


    /**
     * RSA 공개키를 조회한다.
     *
     * @author chauki
     * @version 1.0
     **/
    @GetMapping("/pubkey")
    @ResponseWrapper
    public Map getPublicKey() {
        return jwtAuthenticationService.getPublicKey();
    }

}
