package com.example.lifeshare.api.user.controller;

import com.example.lifeshare.api.user.model.UserDto;
import com.example.lifeshare.api.user.service.UserService;
import com.example.lifeshare.cmm.annotation.ResponseWrapper;
import com.example.lifeshare.cmm.exception.ServiceException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 유저정보 controller 클래스
 *
 * @author parksujin
 * @version 1.0
 **/
@Tag(name = "유저정보 API")
@CrossOrigin
@RequestMapping("/api/user")
@RestController
public class UserController {

    /**
     * UserService 객체
     */
    private final UserService userService;

    /**
     * UserController 생성자
     * - Field Injection 방지
     *
     * @param userService UserService 객체
     * @author parksujin
     * @version 1.0
     **/
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 유저 상세 정보를 조회한다.
     *
     * @param userId 사용자 아이디
     * @return UserDto 사용자 정보
     * @author parksujin
     * @version 1.0
     **/
    @Operation(summary = "유저 상세 정보 조회 API", description = "유저 상세 정보를 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(description = "Bad Request", example = "Bad Request "))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(description = "Unauthorized", example = "Unauthorized"))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(description = "Not Found", example = "Not Found"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(description = "Internal Server Error", example = "Internal Server Error")))
    })
    @GetMapping("/{userId}")
    @ResponseWrapper
    public UserDto reqGetUserInfo(@Parameter(name = "userId", description = "필수 : userId") @PathVariable("userId") String userId) throws ServiceException {
        return userService.reqGetUserInfo(userId);
    }

    /**
     * 유저 정보를 등록한다.
     *
     * @param userDto UserDto 객체
     * @return Boolean 등록 성공 여부
     * @author parksujin
     * @version 1.0
     **/
    @Operation(summary = "유저 정보 등록 API", description = "유저 정보를 등록한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(description = "Bad Request", example = "Bad Request "))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(description = "Unauthorized", example = "Unauthorized"))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(description = "Not Found", example = "Not Found"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(description = "Internal Server Error", example = "Internal Server Error")))
    })
    @PostMapping("/join")
    @ResponseWrapper
    public Boolean reqPostUserInfo(@Parameter(name = "userDto", description = "필수 : adminId, adminPw, adminNm") @RequestBody UserDto userDto) throws ServiceException {
        return userService.reqPostUserInfo(userDto);
    }

    /**
     * 유저 아이디 중복체크를 한다.
     *
     * @param userId 사용자 아이디
     * @return Boolean 중복 여부
     * @author parksujin
     * @version 1.0
     **/
    @Operation(summary = "아이디 중복체크 API ", description = "아이디 중복체크를 수행한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(description = "Bad Request", example = "Bad Request "))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(description = "Unauthorized", example = "Unauthorized"))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(description = "Not Found", example = "Not Found"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(description = "Internal Server Error", example = "Internal Server Error")))
    })
    @Parameters(value = {
            @Parameter(name = "userId", description = "유저 아이디", required = true)
    })
    @GetMapping("/check/id")
    @ResponseWrapper
    public Boolean reqGetUserIdCheck(@Parameter(name = "userId", description = "필수 : userId")
                                     @RequestParam("userId") String userId) throws ServiceException {
        return userService.reqGetUserIdCheck(userId);
    }

    /**
     * 이메일 중복 체크를 한다.
     *
     * @param email email
     * @return Boolean 중복 여부
     * @author taejin
     * @version 1.0
     **/
    @Operation(summary = "이메일 중복체크 API ", description = "이메일 중복체크를 수행한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(description = "Bad Request", example = "Bad Request "))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(description = "Unauthorized", example = "Unauthorized"))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(description = "Not Found", example = "Not Found"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(description = "Internal Server Error", example = "Internal Server Error")))
    })
    @Parameters(value = {
            @Parameter(name = "email", description = "이메일", required = true)
    })
    @GetMapping("/check/email")
    @ResponseWrapper
    public Boolean reqGetUserEmailCheck(@Parameter(name = "email", description = "필수 : email")
                                        @RequestParam("email") String email) throws ServiceException {
        return userService.reqGetUserEmailCheck(email);
    }

    /**
     * 비밀번호를 초기화한다.
     *
     * @param userDto UserDto 객체
     * @return Boolean 초기화 여부
     * @author parksujin
     * @version 1.0
     **/
    @Operation(summary = "비밀번호 초기화 API ", description = "비밀번호를 초기화한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(description = "Bad Request", example = "Bad Request "))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(description = "Unauthorized", example = "Unauthorized"))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(description = "Not Found", example = "Not Found"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(description = "Internal Server Error", example = "Internal Server Error")))
    })
    @Parameters(value = {
            @Parameter(name = "userId", description = "사용자 아이디", required = true),
            @Parameter(name = "newPwd", description = "새 비밀번호", required = true),
    })
    @PutMapping("/pwd/reset")
    @ResponseWrapper
    public Boolean reqPutUserPwdReset(@Parameter(name = "userDto", description = "필수 : userId, newPwd") @RequestBody UserDto userDto) throws ServiceException {
        return userService.reqPutUserPwdReset(userDto);
    }

    /**
     * @param
     * @return Boolean : 조회 여부
     * @throws ServiceException : 예외
     * @funcName : reqGetUserList
     * @description : 조직별 유저 목록을 조회한다.
     * @date : 2022-05-20 오전 10:12
     * @author : taejin
     * @history :
     * @see
     **/
    @Operation(summary = "유저 목록 조회 API", description = "조직별 유저 목록을 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(description = "Bad Request", example = "Bad Request"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(description = "Unauthorized", example = "Unauthorized"))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(description = "Not Found", example = "Not Found"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(description = "Internal Server Error", example = "Internal Server Error")))
    })
    @Parameters(value = {
            @Parameter(name = "userId", description = "사용자 아이디", required = false),
            @Parameter(name = "search", description = "검색", required = false),
            @Parameter(name = "registDt", description = "가입일자 기간", required = false),
            @Parameter(name = "loginDt", description = "로그인일자 기간", required = false),
            @Parameter(name = "order", description = "정렬기준", required = false),
            @Parameter(name = "limit", description = "한페이지에 보여줄 목록 갯수", required = false),
            @Parameter(name = "pageNo", description = "페이지 번호", required = false),
    })
    @GetMapping("")
    @ResponseWrapper
    public Map reqGetUserList(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String registDt,
            @RequestParam(required = false) String loginDt,
            @RequestParam(required = false, defaultValue = "0") int order,
            @RequestParam(required = false, defaultValue = "0") int limit,
            @RequestParam(required = false, defaultValue = "0") int pageNo) throws ServiceException {
        return userService.reqGetUserList(userId, search, registDt, loginDt, order, limit, pageNo);
    }

    /**
     * 비밀번호 초기화 상태를 요청한다.
     *
     * @param userDto UserDto 객체
     * @return Boolean 중복 여부
     * @author taejin
     * @version 1.0
     **/
    @Operation(summary = "비밀번호 초기화 상태 요청 API ", description = "비밀번호 초기화 상태를 요청한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(description = "Bad Request", example = "Bad Request "))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(description = "Unauthorized", example = "Unauthorized"))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(description = "Not Found", example = "Not Found"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(description = "Internal Server Error", example = "Internal Server Error")))
    })
    @Parameters(value = {
            @Parameter(name = "userId", description = "사용자 아이디", required = true),
            @Parameter(name = "pwdResetSttus", description = "비밀번호초기화상태 요청", required = true),
    })
    @PutMapping("/pwd/request")
    @ResponseWrapper
    public Boolean reqPutUserPwdRequest(@Parameter(name = "userDto", description = "필수 : userId, pwdResetSttus")
                                        @RequestBody UserDto userDto) throws ServiceException {
        return userService.reqPutUserPwdRequest(userDto);
    }

    /**
     * 비밀번호 초기화 상태를 승인한다.
     *
     * @param userDto UserDto 객체
     * @return Boolean 중복 여부
     * @author taejin
     * @version 1.0
     **/
    @Operation(summary = "비밀번호 초기화 상태 승인 API ", description = "비밀번호 초기화 상태를 승인한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(description = "Bad Request", example = "Bad Request "))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(description = "Unauthorized", example = "Unauthorized"))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(description = "Not Found", example = "Not Found"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(description = "Internal Server Error", example = "Internal Server Error")))
    })
    @Parameters(value = {
            @Parameter(name = "userId", description = "사용자 아이디", required = true),
            @Parameter(name = "pwdResetSttus", description = "비밀번호초기화상태 요청", required = true),
    })
    @PutMapping("/pwd/approve")
    @ResponseWrapper
    public Boolean reqPutUserPwdApprove(@Parameter(name = "userDto", description = "필수 : userId, pwdResetSttus")
                                        @RequestBody UserDto userDto) throws ServiceException {
        return userService.reqPutUserPwdApprove(userDto);
    }

    /**
     * 유저 정보를 수정한다.
     *
     * @param userDto userDto 객체
     * @return Boolean 수정 성공 여부
     * @author parksujin
     * @version 1.0
     **/
    @Operation(summary = "유저 정보 수정 API", description = "유저 정보를 수정한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(description = "Bad Request", example = "Bad Request "))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(description = "Unauthorized", example = "Unauthorized"))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(description = "Not Found", example = "Not Found"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(description = "Internal Server Error", example = "Internal Server Error")))
    })
    @Parameters(value = {
            @Parameter(name = "userId", description = "사용자 아이디", required = true),
            @Parameter(name = "userNm", description = "사용자명", required = false),
            @Parameter(name = "email", description = "이메일", required = false)
    })
    @PutMapping("")
    @ResponseWrapper
    public Boolean reqPutUserInfo(@Parameter(name = "userDto", description = "필수 : userId")
                                  @RequestBody UserDto userDto,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws ServiceException {
        return userService.reqPutUserInfo(userDto, request, response);
    }

    /**
     * 유저 정보를 삭제한다.
     *
     * @param userIdList 사용자 아이디 목록
     * @return Boolean 삭제 성공 여부
     * @author taejin
     * @version 1.0
     **/
    @Operation(summary = "유저 정보 삭제 API", description = "유저 정보를 삭제한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(description = "Bad Request", example = "Bad Request "))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(description = "Unauthorized", example = "Unauthorized"))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(description = "Not Found", example = "Not Found"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(description = "Internal Server Error", example = "Internal Server Error")))
    })
    @Parameters(value = {
            @Parameter(name = "userId", description = "사용자ID", required = false),
    })
    @DeleteMapping("")
    @ResponseWrapper
    public Boolean reqDeleteUserList(@RequestParam List<String> userIdList) throws ServiceException {
        return userService.reqDeleteUserList(userIdList);
    }

    /**
     * 유저 아이디 정보를 조회한다.
     *
     * @param userNm 사용자명
     * @param email  이메일
     * @return Map<String, Object> 사용자 정보
     * @author taejin
     * @version 1.0
     **/
    @Operation(summary = "유저 아이디 정보를 조회 API", description = "유저 아이디 정보를 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(description = "Bad Request", example = "Bad Request "))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(description = "Unauthorized", example = "Unauthorized"))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(description = "Not Found", example = "Not Found"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(description = "Internal Server Error", example = "Internal Server Error")))
    })
    @Parameters(value = {
            @Parameter(name = "userNm", description = "유저 이름", required = true),
            @Parameter(name = "email", description = "이메일", required = true),
    })
    @GetMapping("/search")
    @ResponseWrapper
    public Map<String, Object> reqGetUserId(@RequestParam("userNm") String userNm,
                                            @RequestParam("email") String email) throws ServiceException {
        return userService.reqGetUserId(userNm, email);
    }

    /**
     * 비밀번호를 변경한다.
     *
     * @param userDto UserDto 객체
     * @return Boolean 비밀번호 변경 여부
     * @author parksujin
     * @version 1.0
     **/
//    @Operation(summary = "비밀번호 변경 API(로그인 후) ", description = "비밀번호를 변경한다.")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Success"),
//            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(description = "Bad Request", example = "Bad Request "))),
//            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(description = "Unauthorized", example = "Unauthorized"))),
//            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(description = "Not Found", example = "Not Found"))),
//            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(description = "Internal Server Error", example = "Internal Server Error")))
//    })
//    @Parameters(value = {
//            @Parameter(name = "newPwd", description = "새 비밀번호", required = true),
//            @Parameter(name = "pwd", description = "비밀번호", required = true),
//    })
//    @PutMapping("/pwd/change")
//    @ResponseWrapper
//    public Boolean reqPutUserPwdChange(@Parameter(name = "userDto", description = "필수 : pwd, newPwd")
//                                       @RequestBody UserDto userDto,
//                                       HttpServletRequest request,
//                                       HttpServletResponse response) throws ServiceException {
//        return userService.reqPutUserPwdChange(userDto, request, response);
//    }
}
