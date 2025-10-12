package com.example.lifeshare.config;

import com.example.lifeshare.cmm.jwt.AuthUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 로깅 설정 클래스
 *
 * @author chauki
 * @version 1.0
 ** 1. log를 쌓기 위한 config 정보 설정
 ** 2. api 요청에 대한 log 문자열 정의
 **/
@Configuration
@EnableAspectJAutoProxy
@Aspect
public class LoggingConfig {
    /**
     * 로그 객체
     */
    private static final Logger logger = LoggerFactory.getLogger(LoggingConfig.class);

    /**
     * REQUEST 로그 포맷
     */
    private static final String CONTROLLER_REQUEST_LOG_FORMAT = "=========API REQUEST======> Client : {}({}), {} {}, {} : params = {}";

    /**
     * RESPONSE 로그 포맷
     */
    private static final String CONTROLLER_RESPONSE_LOG_FORMAT = "=========API RESPONSE=====> Client : {}({}), {} {}, {}({}) : return = {}";

    /**
     * SERVICE 로그 포맷
     */
    private static final String SERVICE_LOG_FORMAT = "=========SERVICE LOG======> Function : {}({}) => {}";

    /**
     * EXCEPTION 로그 포맷
     */
    private static final String EXCEPTION_LOG_FORMAT = "=========EXCEPTION LOG====> Exception at : {}({})";

    /**
     * TIMESTAMP 로그 포맷
     */
    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * AuthUtil 객체
     */
    private final AuthUtil authUtil;

    /**
     * 클라이언트 명
     */
    private String clientNm;

    /**
     * 클라이언트 주소
     */
    private String clientAddr;

    /**
     * 함수 명
     */
    private String funcNm;

    /**
     * 요청 URI
     */
    private String requestUri;

    /**
     * 요청 메소드
     */
    private String httpMethod;

    /**
     * 요청 상태
     */
    private String httpStatus;

    /**
     * 파라미터 정보
     */
    private String params;

    /**
     * controller 포인트컷
     */
    @Pointcut("execution(* com.web.ns_mng_api.api.*.controller.*Controller.*(..)) && !@annotation(com.web.ns_mng_api.cmm.annotation.NoLogging)")
    private static void controllerPoint() {
    }

    /**
     * service 포인트컷
     */
    @Pointcut("execution(* com.web.ns_mng_api.api.*.service.*Service.*(..)) && !@annotation(com.web.ns_mng_api.cmm.annotation.NoLogging)")
    private static void servicePoint() {
    }

    /**
     * service 포인트컷
     */
    @Autowired
    public LoggingConfig(AuthUtil authUtil) {
        this.authUtil = authUtil;
    }

    /**
     * 컨트롤러 실행 전 로깅 설정
     *
     * @param joinPoint JoinPoint 객체
     * @return void
     * @author chauki
     * @version 1.0
     * * 1. before 사태의 logging 처리
     **/
    @Before(value = "controllerPoint()")
    private void loggingControllerBefore(JoinPoint joinPoint) {
        setRequestInfo();
        setFuncInfo(joinPoint);
        logger.info(CONTROLLER_REQUEST_LOG_FORMAT
                , clientNm
                , clientAddr
                , httpMethod
                , requestUri
                , funcNm
                , params
        );
    }

    /**
     * 컨트롤러 정상 동작 시 로깅 설정
     *
     * @param joinPoint JoinPoint 객체
     * @param result    Object 객체
     * @return void
     * @author chauki
     * @version 1.0
     * * 1. after 상태의 logging 처리
     **/
    @AfterReturning(value = "controllerPoint()", returning = "result")
    private void loggingControllerAfterReturning(JoinPoint joinPoint, Object result) {
        setRequestInfo();
        setFuncInfo(joinPoint);
        String returns = result.toString();
        logger.info(CONTROLLER_RESPONSE_LOG_FORMAT
                , clientNm
                , clientAddr
                , httpStatus
                , requestUri
                , funcNm
                , params
                , returns
        );
    }

    /**
     * 컨트롤러 예외 발생 시 로깅 설정
     *
     * @param joinPoint JoinPoint 객체
     * @param exception Throwable 객체
     * @return void
     * @author chauki
     * @version 1.0
     * * 1. after throwing logging 처리
     **/
    @AfterThrowing(value = "controllerPoint()", throwing = "exception")
    private void loggingControllerAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        setFuncInfo(joinPoint);
        logger.error(EXCEPTION_LOG_FORMAT
                , funcNm
                , params);
        logger.error("", exception);
    }

    /**
     * 서비스 정상 동작 시 로깅 설정
     *
     * @param joinPoint JoinPoint 객체
     * @param result    Object 객체
     * @return void
     * @author chauki
     * @version 1.0
     * * 1. after return logging 처리
     **/
    @AfterReturning(value = "servicePoint()", returning = "result")
    private void loggingServiceAfterReturning(JoinPoint joinPoint, Object result) {
        setFuncInfo(joinPoint);
        String returns = result.toString();
        logger.info(SERVICE_LOG_FORMAT
                , funcNm
                , params
                , returns
        );
    }

    /**
     * 서비스 예외 발생 시 로깅 설정
     *
     * @param joinPoint JoinPoint 객체
     * @param exception Throwable 객체
     * @return void
     * @author chauki
     * @version 1.0
     * * 1. after throwing logging 처리
     **/
    @AfterThrowing(value = "servicePoint()", throwing = "exception")
    private void loggingServiceAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        setFuncInfo(joinPoint);
        logger.error(EXCEPTION_LOG_FORMAT
                , funcNm
                , params);
        logger.error("", exception);
    }

    /**
     * 요청정보를 설정한다.
     *
     * @return void
     * @author chauki
     * @version 1.0
     * * 1. request 정보 설정
     * * 2. client 명, 주소, URI, Method, Status 설정
     **/
    private void setRequestInfo() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        clientNm = authUtil.getUsername(request, response);
        clientAddr = request.getRemoteAddr();
        requestUri = request.getRequestURI();
        httpMethod = request.getMethod();
        httpStatus = Integer.toString(response.getStatus());
    }

    /**
     * 함수 정보 설정
     *
     * @param joinPoint JoinPoint 객체
     * @return void
     * @author chauki
     * @version 1.0
     * * 1. function 명
     * * 2. 파라미터 정보 설정
     **/
    private void setFuncInfo(JoinPoint joinPoint) {
        funcNm = joinPoint.getTarget().getClass().getSimpleName() + "." + joinPoint.getSignature().getName();
        params = getParamString(joinPoint);
    }

    /**
     * 요청 파라미터 정보 문자열 변환 (서블릿 정보 제외)
     *
     * @param joinPoint JoinPoint 객체
     * @return String 파라미터 문자열
     * @author chauki
     * @version 1.0
     * * 1. 요청 파라미터 정보 전달
     * * 2. 파라미터 정보 파싱
     **/
    private String getParamString(JoinPoint joinPoint) {
        Map<String, Object> param = new HashMap<>();
        Object[] paramValues = joinPoint.getArgs();
        String[] paramNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
        for (int i = 0; i < paramNames.length; i++) {
            if (paramValues[i] instanceof HttpServletRequest ||
                    paramValues[i] instanceof HttpServletResponse) {
                continue;
            }
            param.put(paramNames[i], paramValues[i]);
        }
        return param.toString();
    }
}
