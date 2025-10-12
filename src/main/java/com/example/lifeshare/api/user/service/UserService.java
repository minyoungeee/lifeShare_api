package com.example.lifeshare.api.user.service;

import com.example.lifeshare.cmm.jwt.AuthUtil;
import com.nhncorp.lucy.security.xss.XssPreventer;
import com.example.lifeshare.api.user.mapper.UserMapper;
import com.example.lifeshare.api.user.model.UserDto;
import com.example.lifeshare.api.user.model.UserListReqDto;
import com.example.lifeshare.cmm.exception.ServiceException;
import com.example.lifeshare.config.AES128;
import com.example.lifeshare.config.RSA;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 유저정보 service 클래스
 *
 * @author parksujin
 * @version 1.0
 **/
@Slf4j
@Service
public class UserService {

    /**
     * UserMapper 객체
     */
    private final UserMapper userMapper;

    /**
     * AuthUtil 객체
     */
    private final AuthUtil authUtil;

    /**
     * AES128 객체
     */
    private final AES128 aes128;

    /**
     * RSA 객체
     */
    private final RSA rsa;

    /**
     * passwordEncoder 객체
     */
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * UserService 생성자
     * - Field Injection 방지
     *
     * @param userMapper      userMapper 객체
     * @param authUtil        AuthUtil 객체
     * @param rsa             RSA 객체
//     * @param passwordEncoder BCryptPasswordEncoder 클래스
     * @author parksujin
     * @version 1.0
     **/
    @Autowired
    public UserService(UserMapper userMapper,
                       AuthUtil authUtil,
                       AES128 aes128,
                       RSA rsa,
                       BCryptPasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.authUtil = authUtil;
        this.aes128 = aes128;
        this.rsa = rsa;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * @param userId : userId
     * @return UserDto : 유저 정보
     * @throws ServiceException : 예외
     * @funcName : reqGetUserPwd
     * @description : 유저 비밀번호를 조회한다.
     * @date : 2022-05-02 오후 1:04
     * @author : parksujin
     * @history :
     * @see
     **/
    public UserDto reqGetUserPwd(@Param("adminId") String userId, Boolean auth) throws ServiceException {
        try {
            UserDto userDto = new UserDto();
            userDto.setAdminId(userId);
            return userMapper.selectOneUserInfo(userDto);
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 유저 상세 정보를 조회한다.
     *
     * @param userId 사용자 아이디
     * @return UserDto 사용자 정보
     * @author parksujin
     * @version 1.0
     **/
    public UserDto reqGetUserInfo(String userId) throws ServiceException {
        try {
            UserDto userDto = new UserDto();
            userDto.setAdminId(userId);
            userDto = userMapper.selectOneUserInfo(userDto);

            //이메일 복호화
            /*if (userDto != null) {
                userDto.setEmail(aes128.decrypt(userDto.getEmail()));
                return userDto;
            }*/
            return userDto;
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 유저 정보를 등록한다.
     *
     * @param userDto UserDto 객체
     * @return Boolean 등록 성공 여부
     * @author parksujin
     * @version 1.0
     **/
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public Boolean reqPostUserInfo(UserDto userDto) throws ServiceException {
        try {
            //xss 필터
//            userDto.setAdminId(XssPreventer.escape(userDto.getAdminId()));
//            userDto.setAdminPw(XssPreventer.escape(userDto.getAdminPw()));
//            userDto.setAdminNm(XssPreventer.escape(userDto.getAdminNm()));
//            userDto.setEmail(XssPreventer.escape(userDto.getEmail()));
//            userDto.setUserGrad(XssPreventer.escape(userDto.getUserGrad()));

            //비밀번호 암호화
            // BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//            userDto.setAdminPw(passwordEncoder.encode(this.rsa.decrypt(userDto.getAdminPw())));

            //이메일 암호화
//            userDto.setEmail(aes128.encrypt(this.rsa.decrypt(userDto.getEmail())));

            //사용자 아이디
//            userDto.setAdminId(this.rsa.decrypt(userDto.getAdminId()));

            int result = userMapper.insertUser(userDto);
            if (result > 0) {
                return true;
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 유저 아이디 중복체크를 한다.
     *
     * @param userId 사용자 아이디
     * @return Boolean 중복 여부
     * @author parksujin
     * @version 1.0
     **/
    public Boolean reqGetUserIdCheck(String userId) throws ServiceException {
        try {
            int cnt = userMapper.selectOneUserIdDuplCheck(userId);
            if (cnt > 0) {
                return false;
            }
            return true;
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 이메일 중복 체크를 한다.
     *
     * @param email email
     * @return Boolean 중복 여부
     * @author taejin
     * @version 1.0
     **/
    public Boolean reqGetUserEmailCheck(String email) throws ServiceException {
        try {
            String encryptedEmail = aes128.encrypt(email); //이메일 암호화
            int cnt = userMapper.selectOneUserEmailDuplCheck(encryptedEmail);
            if (cnt > 0) {
                return false;
            }
            return true;
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 비밀번호를 초기화한다.
     *
     * @param userDto UserDto 객체
     * @return Boolean 초기화 여부
     * @author parksujin
     * @version 1.0
     **/
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public Boolean reqPutUserPwdReset(UserDto userDto) throws ServiceException {
        try {
            //xss 필터
            userDto.setAdminId(XssPreventer.escape(userDto.getAdminId()));
            userDto.setNewPwd(XssPreventer.escape(userDto.getNewPwd()));

            //비밀번호 암호화
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            userDto.setNewPwd(passwordEncoder.encode(userDto.getNewPwd()));

            int result = userMapper.updateUserPwdReset(userDto);
            if (result > 0) {
                return true;
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 조직별 유저 목록을 조회한다.
     *
     * @param userId   사용자 아이디
     * @param search   검색어
     * @param registDt 가입일자 기간
     * @param loginDt  로그인일자 기간
     * @param order    정렬기준
     * @param limit    한 페이지당 요청 건수
     * @param pageNo   페이지 번호
     * @return Map<String, Object> 사용자 목록
     * @author taejin
     * @version 1.0
     **/
    public Map<String, Object> reqGetUserList(String userId,
                                              String search,
                                              String registDt,
                                              String loginDt,
                                              int order,
                                              int limit,
                                              int pageNo) throws ServiceException {
        try {

            Map<String, Object> resultMap = new HashMap<>();
            UserListReqDto userListReqDto = UserListReqDto.builder()
                    .userId(userId)
                    .search(search)
                    .registDt(registDt)
                    .loginDt(loginDt)
                    .order(order)
                    .limit(limit)
                    .pageNo(pageNo)
                    .build();

            List<UserDto> usersList = userMapper.selectListUser(userListReqDto);

            //이메일 복호화
            for (int i = 0; i < usersList.size(); i++) {
                String decEmail = aes128.decrypt(usersList.get(i).getEmail()); // email 복호화
                usersList.get(i).setEmail(decEmail);
            }
            //사용자 목록 개수 조회
            resultMap.put("totalCnt", userMapper.selectOneUserCnt(userListReqDto));
            resultMap.put("userList", usersList);

            return resultMap;

        } catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 유저 정보를 수정한다.
     *
     * @param userDto userDto 객체
     * @return Boolean 수정 성공 여부
     * @author parksujin
     * @version 1.0
     **/
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public Boolean reqPutUserInfo(UserDto userDto,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws ServiceException {
        try {
            //xss 필터
            userDto.setAdminId(XssPreventer.escape(userDto.getAdminId()));
            userDto.setAdminNm(XssPreventer.escape(userDto.getAdminNm()));
//            userDto.setEmail(XssPreventer.escape(userDto.getEmail()));
//            userDto.setUserGrad(XssPreventer.escape(userDto.getUserGrad()));
//            userDto.setApplvlYn(XssPreventer.escape(userDto.getApplvlYn()));

            //이메일 암호화
            userDto.setEmail(aes128.encrypt(this.rsa.decrypt(userDto.getEmail())));

            //사용자 아이디
            userDto.setAdminId(this.rsa.decrypt(userDto.getAdminId()));

            userDto.setUpdtId(authUtil.getUsername(request, response));

            int result = userMapper.updateUserInfo(userDto);
            if (result > 0) {
                return true;
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 비밀번호 초기화 상태를 요청한다.
     *
     * @param userDto UserDto 객체
     * @return Boolean 중복 여부
     * @author taejin
     * @version 1.0
     **/
    @Transactional(rollbackFor = {Exception.class})
    public Boolean reqPutUserPwdRequest(UserDto userDto) throws ServiceException {
        try {
            int result = userMapper.updateUserPwdReq(userDto);
            if (result > 0) {
                return true;
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 비밀번호 초기화 상태를 승인한다.
     *
     * @param userDto UserDto 객체
     * @return Boolean 중복 여부
     * @author taejin
     * @version 1.0
     **/
    @Transactional(rollbackFor = {Exception.class})
    public Boolean reqPutUserPwdApprove(UserDto userDto) throws ServiceException {
        try {
            int result = userMapper.updateUserPwdAprv(userDto);
            if (result > 0) {
                return true;
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param userIdList : 유저 목록
     * @return Boolean : 유저 정보 삭제 성공 여부
     * @throws ServiceException : 예외
     * @funcName : reqDeleteUserList
     * @description : 유저 정보를 삭제한다.
     * @date : 2022-05-26 오전 10:07
     * @author : taejin
     * @history :
     * @see
     **/
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public Boolean reqDeleteUserList(List<String> userIdList) throws ServiceException {
        try {
            int result = userMapper.deleteUserList(userIdList);
            if (result > 0) {
                return true;
            }
            return false;
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
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
    public Map<String, Object> reqGetUserId(String userNm, String email) throws ServiceException {
        try {
            Map<String, Object> resultMap = new HashMap<>();
            UserDto userDto = new UserDto();
            //xss 필터
            userDto.setAdminNm(XssPreventer.escape(userNm));
            userDto.setEmail(XssPreventer.escape(email));

            //이메일 암호화
            userDto.setEmail(aes128.encrypt(userDto.getEmail()));
            UserDto userInfo = userMapper.selectOneUserId(userDto);
            if (userInfo != null) {
                resultMap.put("exist", true);
                resultMap.put("userInfo", userInfo);
            } else {
                resultMap.put("exist", false);
                resultMap.put("userInfo", null);
            }
            return resultMap;
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 비밀번호를 변경한다.
     *
     * @param userDto UserDto 객체
     * @return Boolean 비밀번호 변경 여부
     * @author parksujin
     * @version 1.0
     **/
//    @Transactional(rollbackFor = {Exception.class, Error.class})
//    public Boolean reqPutUserPwdChange(UserDto userDto,
//                                       HttpServletRequest request,
//                                       HttpServletResponse response) throws ServiceException {
//        try {
//            //xss 필터
//            userDto.setAdminPw(XssPreventer.escape(userDto.getAdminPw()));
//            userDto.setNewPwd(XssPreventer.escape(userDto.getNewPwd()));
//
//            String userId = authUtil.getUsername(request, response);
//            String currentPwd = reqGetUserPwd(userId, true).getAdminPw(); //인코딩된 현재 비밀번호
//
//            if (currentPwd != null) {
//                String pwd = this.rsa.decrypt(userDto.getAdminPw()); //현재 raw 비밀번호
//                //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
////                Boolean isMatches = passwordEncoder.matches(pwd, currentPwd);
//                if (isMatches) {
//                    userDto.setAdminId(userId);
//                    userDto.setNewPwd(passwordEncoder.encode(this.rsa.decrypt(userDto.getNewPwd())));
//                    userMapper.updateUserPwd(userDto);
//                    return true;
//                } else {
//                    return false;
//                }
//            } else {
//                return false;
//            }
//        } catch (DataAccessException e) {
//            log.error(e.getMessage());
//            throw new ServiceException(e.getMessage());
//        }
//    }
}

