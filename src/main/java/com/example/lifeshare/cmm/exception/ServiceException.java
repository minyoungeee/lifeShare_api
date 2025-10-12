package com.example.lifeshare.cmm.exception;

/**
 * @className : ServiceException
 * @description : 서비스 Exception 샘플
 * @autor : chauki
 * @date : 2020-12-07
 */
public class ServiceException extends RuntimeException {

    private static final String INTERNAL_SERVER_ERROR_MSG = "서버에 내부 오류가 생겨 요청을 끝까지 처리하지 못했습니다.";

    /**
     * @name : ServiceException
     * @description : 서비스 Exception 생성자
     * @param message : 예외 메시지
     */
    public ServiceException(String message) {
        super(message);
    }

    /**
     * @name : ServiceException
     * @description : 서비스 Exception 생성자
     * @param e : 예외
     */
    public ServiceException(Exception e) {
//        super(INTERNAL_SERVER_ERROR_MSG);
        super(e);
    }

    public ServiceException() {
        super(INTERNAL_SERVER_ERROR_MSG);
    }
}
