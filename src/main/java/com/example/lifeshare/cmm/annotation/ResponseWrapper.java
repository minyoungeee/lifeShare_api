package com.example.lifeshare.cmm.annotation;

import java.lang.annotation.*;


/**
 * @className : ResponseWrapper
 * @description : 응답 Wrapper 클래스
 * - 커스텀 어노테이션으로 적용
 * @autor : chauki
 * @date : 2020-10-22
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseWrapper {
}
