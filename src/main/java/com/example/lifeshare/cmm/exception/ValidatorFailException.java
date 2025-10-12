package com.example.lifeshare.cmm.exception;

/**
 * @className : ServiceException
 * @description : 서비스 Exception 샘플
 * @autor : yh.kim
 * @date : 2022-04-14
 */
public class ValidatorFailException extends RuntimeException {

    /**
     * @name : ServiceException
     * @description : 서비스 Exception 생성자
     * @param message : 예외 메시지
     */
    public ValidatorFailException(String message) {
        super(message);
    }

    /**
     * @name : ServiceException
     * @description : 서비스 Exception 생성자
     * @param e : 예외
     */
    public ValidatorFailException(Exception e) {
        super(e);
    }
}
