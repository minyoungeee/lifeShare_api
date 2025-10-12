package com.example.lifeshare.config;
//import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * RSA 암호화 클래스
 *
 * @author chauki
 * @version 1.0
 ** 1. 로그인 민감정보 RSA 암호화 설정
 ** 2. encrypt / decrypt 기능 제공
 **/
@Component
@Slf4j
public class RSA {

    /**
     * RSAPublicKey 객체
     */
    private RSAPublicKey publicKey;

    /**
     * RSAPrivateKey 객체
     */
    private RSAPrivateKey privateKey;

    /**
     * public 키
     */
    @Value("${keySet.key1}")
    private String sPublicKey;

    /**
     * private 키
     */
    @Value("${keySet.key2}")
    private String sPrivateKey;

    /**
     * RSA 생성자
     *
     * @author chauki
     * @version 1.0
     ** 1. RSA 초기화 수행
     ** 2. public / private key 설정
     **/
    public RSA() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024, new SecureRandom());
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            this.publicKey = (RSAPublicKey) keyPair.getPublic();
            this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
        } catch (NoSuchAlgorithmException e) {
            log.error("generate rsa key error");
        }
    }

    /**
     * public 키를 가져온다.
     *
     * @return String public 키
     * @author chauki
     * @version 1.0
     ** 1. publick 키 조회
     **/
    public String getPublicKey() {
        String tmpPublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        if (sPublicKey != null) {
            tmpPublicKey = sPublicKey;
        }
        return tmpPublicKey;
    }

    /**
     * 암호화를 수행한다.
     *
     * @param str 키 정보
     * @return String 암호화 값
     * @author chauki
     * @version 1.0
     ** 1. 문자열을 입력받는다.
     ** 2. 문자열을 암호화 한다.
     ** 3. 암호화된 문자열을 반환한다.
     **/
    public String encrypt(String str) {
        String outStr = null;
        Cipher cipher = null;
        try {
            PublicKey tmpPublicKey = publicKey;
            if (sPublicKey != null) {
                try {
                    PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(sPublicKey));
                    KeyFactory kf = KeyFactory.getInstance("RSA");
                    tmpPublicKey = kf.generatePublic(ks);
                } catch (InvalidKeySpecException e) {
                    /* NS-FIX */
                    log.error("InvalidKeySpecException occurred");
                }
            }
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, tmpPublicKey);
            byte[] inputByte = cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
            outStr = new String(Base64.getEncoder().encode(inputByte));
        } catch (NoSuchPaddingException e) {
            /* NS-FIX */
            log.error("NoSuchPaddingException occurred");
        } catch (NoSuchAlgorithmException e) {
            /* NS-FIX */
            log.error("NoSuchAlgorithmException occurred");
        } catch (InvalidKeyException e) {
            /* NS-FIX */
            log.error("InvalidKeyException occurred");
        } catch (IllegalBlockSizeException e) {
            /* NS-FIX */
            log.error("IllegalBlockSizeException occurred");
        } catch (BadPaddingException e) {
            /* NS-FIX */
            log.error("BadPaddingException occurred");
        }
        return outStr;
    }

    /**
     * 복화화를 수행한다.
     *
     * @param str 암호화 값
     * @return String 복호화 값
     * @author chauki
     * @version 1.0
     ** 1. 문자열을 입력받는다.
     ** 2. 문자열을 복호화 한다.
     ** 3. 복호화된 문자열을 반환한다.
     **/
    public String decrypt(String str) {
        byte[] inputByte = Base64.getDecoder().decode(str); //Base64.decode(str);
        String outStr = null;
        Cipher cipher = null;
        try {
            PrivateKey tmpPrivateKey = privateKey;
            if (sPrivateKey != null) {
                try {
                    PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(sPrivateKey));
                    KeyFactory kf = KeyFactory.getInstance("RSA");
                    tmpPrivateKey = kf.generatePrivate(ks);
                } catch (InvalidKeySpecException e) {
                    /* NS-FIX */
                    log.error("InvalidKeySpecException occurred");
                }
            }
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, tmpPrivateKey);
            outStr = new String(cipher.doFinal(inputByte));
        } catch (NoSuchPaddingException e) {
            /* NS-FIX */
            log.error("NoSuchPaddingException occurred");
        } catch (NoSuchAlgorithmException e) {
            /* NS-FIX */
            log.error("NoSuchAlgorithmException occurred");
        } catch (InvalidKeyException e) {
            /* NS-FIX */
            log.error("InvalidKeyException occurred");
        } catch (IllegalBlockSizeException e) {
            /* NS-FIX */
            log.error("IllegalBlockSizeException occurred");
        } catch (BadPaddingException e) {
            /* NS-FIX */
            log.error("BadPaddingException occurred");
        }
        return outStr;
    }

}
