package com.example.lifeshare.api.user.mapper;

import com.example.lifeshare.api.user.model.*;
import com.example.lifeshare.cmm.exception.ServiceException;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @className : UserMapper
 * @description : 유저정보 mapper 클래스
 * @date : 2022-05-02 오후 1:04
 * @author : parksujin
 * @version : 1.0.0
 * @see
 * @history :
**/
@Mapper
public interface UserMapper {

    /**
     * @funcName : selectOneUserInfo
     * @description : 유저 정보를 조회한다.
     * @param userDto : UserDto 객체
     * @return userDto : 유저 정보
     * @exception ServiceException : 예외
     * @date : 2022-05-02 오후 1:04
     * @author : parksujin
     * @see
     * @history :
    **/
    UserDto selectOneUserInfo(UserDto userDto) throws ServiceException;

     /**
     * @param userDto : UserDto 객체
     * @return Boolean : 등록 성공 여부
     * @throws ServiceException : 예외
     * @funcName : insertUser
     * @description : 유저 정보를 등록한다.
     * @date : 2022-05-02 오후 1:04
     * @author : parksujin
     * @history :
     * @see
     **/
    int insertUser(UserDto userDto) throws ServiceException;

    /**
     * @funcName : selectOneUserIdDuplCheck
     * @description : 아이디 중복을 체크한다.
     * @param userId : userId
     * @return : int
     * @exception ServiceException : 예외
     * @date : 2022-05-19 오후 2:47
     * @author : parksujin
     * @see
     * @history :
    **/
    int selectOneUserIdDuplCheck(String userId) throws ServiceException;

    /**
     * @funcName : selectOneUserEmailDuplCheck
     * @description : 이메일 중복을 조회한다.
     * @param :
     * @return :
     * @exception ServiceException : 예외
     * @date : 2022-05-19 오후 2:47
     * @author : taejin
     * @see
     * @history :
    **/
    int selectOneUserEmailDuplCheck(String email) throws ServiceException;

    /**
     * @funcName : updateLoginDt
     * @description : 최근 접속일자를 업데이트 한다.
     * @param userId : userId
     * @return int : 변경 여부
     * @exception ServiceException : 예외
     * @date : 2022-05-19 오후 2:47
     * @author : parksujin
     * @see
     * @history :
    **/
    int updateUserLoginDt(String userId) throws ServiceException;

    /**
     * @funcName : updateLogoutDt
     * @description : 로그아웃 일자를 업데이트 한다.
     * @param userId : userId
     * @return int : 변경 여부
     * @exception ServiceException : 예외
     * @date : 2022-05-19 오후 5:47
     * @author : parksujin
     * @see
     * @history :
    **/
    int updateUserLogoutDt(String userId) throws ServiceException;

    /**
     * @funcName : updateUserPwdReset
     * @description : 비밀번호를 초기화한다.
     * @param userDto : UserDto 객체
     * @return Boolean : 중복 여부
     * @exception ServiceException : 예외
     * @date : 2022-05-20 오후 5:00
     * @author : parksujin
     * @see
     * @history :
    **/
    int updateUserPwdReset(UserDto userDto) throws ServiceException;

    /**
     * @funcName : updateUserPwd
     * @description : 비밀번호를 변경한다.
     * @param userDto : UserDto 객체
     * @return Boolean : 중복 여부
     * @exception ServiceException : 예외
     * @date : 2022-05-20 오후 5:00
     * @author : parksujin
     * @see
     * @history :
    **/
    int updateUserPwd(UserDto userDto) throws ServiceException;

    /**
     * @funcName : selectListUser
     * @description : 유저 목록 정보를 조회한다.
     * @param :
     * @return List<UserDto> :
     * @exception ServiceException : 예외
     * @date : 2022-05-20 오전 10:27
     * @author : taejin
     * @see
     * @history :
     **/
    List<UserDto> selectListUser(UserListReqDto userListReqDto) throws ServiceException;

    /**
     * @funcName : selectOneUserCnt
     * @description : 유저 목록 개수를 조회한다.
     * @param :
     * @return :
     * @exception ServiceException : 예외
     * @date : 2022-07-25 오전 9:02
     * @author : taejin
     * @see
     * @history :
    **/
    int selectOneUserCnt(UserListReqDto userListReqDto) throws ServiceException;

    /**
     * @funcName : selectOneUserPwdResetDate
     * @description : 비밀번호 변경 주기를 체크한다.
     * @param userId : userId
     * @return int :
     * @exception ServiceException : 예외
     * @date : 2022-05-23 오후 3:10
     * @author : parksujin
     * @see
     * @history :
    **/
    int selectOneUserPwdResetDate(String userId) throws ServiceException;

    /**
     * @funcName : updateUserInfo
     * @description : 유저 정보를 수정한다.
     * @param userDto : UserDto 객체
     * @return int : 변경 여부
     * @exception ServiceException : 예외
     * @date : 2022-05-24 오후 5:17
     * @author : parksujin
     * @see
     * @history :
    **/
    int updateUserInfo(UserDto userDto) throws ServiceException;

    /**
     * @funcName : updateUserPwdReq
     * @description : 유저가 비밀번호 초기화 상태 요청한다.
     * @param userDto : UserDto 객체
     * @return userDto : UserDto 객체
     * @exception : 유저 비밀번호 초기화 상태 요청 여부
     * @date : 2022-05-24 오후 5:00
     * @author : taejin
     * @see
     * @history :
     **/
    int updateUserPwdReq(UserDto userDto) throws ServiceException;

    /**
     * @funcName : updateUserPwdAprv
     * @description : 유저가 비밀번호 초기화 상태 승인한다.
     * @param userDto : UserDto 객체
     * @return int : 유저 비밀번호 초기화 상태 승인 여부
     * @exception ServiceException : 예외
     * @date : 2022-05-24 오후 5:00
     * @author : taejin
     * @see
     * @history :
     **/
    int updateUserPwdAprv(UserDto userDto) throws ServiceException;

    /**
     * @funcName : deleteUserList
     * @description : 유저 정보를 삭제한다.
     * @param userIdList : 유저 목록
     * @return int : 유저 정보 삭제 성공 여부
     * @exception ServiceException : 예외
     * @date : 2022-05-25 오후 6:01
     * @author : taejin
     * @see
     * @history :
    **/
    int deleteUserList(List<String> userIdList) throws ServiceException;

    /**
     * @funcName : selectOneUserId
     * @description : 유저 아이디 정보를 조회한다.
     * @param userDto : UserDto 객체
     * @return userDto : 유저 정보
     * @exception ServiceException : 예외
     * @date : 2022-05-26 오전 10:07
     * @author : taejin
     * @see
     * @history :
    **/
    UserDto selectOneUserId(UserDto userDto) throws ServiceException;

}
