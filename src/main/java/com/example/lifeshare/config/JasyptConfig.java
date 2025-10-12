package com.example.lifeshare.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Properties 암호화 클래스
 *
 * @author chauki
 * @version 1.0
 ** 1. application.yaml의 민감 정보 암호화 설정
 ** 2. jasypt 암호화
 **/
@Configuration
public class JasyptConfig {

    /**
     * 암호화 객체 생성
     *
     * @return StringEncryptor 암호화 객체
     * @author chauki
     * @version 1.0
     * * 1. jasypt 암호화 설정
     **/
    @Bean(name = "jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        String key = "chub_cms_api_jasypt_key";
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(key); // 암호화할 때 사용하는 키
        config.setAlgorithm("PBEWithMD5AndDES"); // 암호화 알고리즘
        config.setKeyObtentionIterations("1000"); // 반복할 해싱 회수
        config.setPoolSize("1"); // 인스턴스 pool
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator"); // salt 생성 클래스
        config.setStringOutputType("base64"); //인코딩 방식
        encryptor.setConfig(config);
        return encryptor;
    }
}
