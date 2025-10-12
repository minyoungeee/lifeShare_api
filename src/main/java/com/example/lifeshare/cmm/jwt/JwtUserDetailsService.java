package com.example.lifeshare.cmm.jwt;

import com.example.lifeshare.api.user.model.UserDto;
import com.example.lifeshare.api.user.service.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * @className : JwtUserDetailsService
 * @description : jwt 유저 api Service 컨트롤러
 * @date : 2021-04-05
 * @author : parksujin
 * @version : 1.0.0
 * @see
 * @history :
**/
@Service
public class JwtUserDetailsService implements UserDetailsService {

    /**
     * UserService 객체
     */
    private final UserService userService;

    /**
     * @name UserService
     * @description UserService 클래스 생성자
     * - Field Injection 문제로 바로 @Autowired를 걸지않고, 생성자로 안전하게 사용
     * @param userService UserService 클래스 객체
     * @author : parksujin
     */
    @Autowired
    public JwtUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    /**
     * @name loadUserByUsername
     * @description 회원 정보를 가져온다
     * @param userId 유저 아이디
     * @author : parksujin
     */
    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        UserDto userDetails = userService.reqGetUserPwd(userId, true);
        if (userDetails != null) {
            return new User(userDetails.getAdminId(), userDetails.getAdminPw(), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with username: " + userId);
        }
    }
}
