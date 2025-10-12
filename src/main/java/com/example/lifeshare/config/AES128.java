package com.example.lifeshare.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.context.annotation.Configuration;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * AES128 클래스
 *
 * @author chauki
 * @version 1.0
 ** 1. 이메일 등 민감한 정보에 대한 암호화 처리
 ** 2. encrypt, decrypt 제공
 **/
@Configuration
@Slf4j
public class AES128 {

	/**
	 * IPS
	 */
	private String ips;

	/**
	 * 키 SPEC
	 */
	private Key keySpec;

	/**
	 * AES128 생성자
	 *
	 * @author chauki
	 * @version 1.0
	 ** 1. AES128 클래스 생성
	 ** 2. IPS / KeySpec 설정
	 **/
	public AES128() {
		String key = "AES128RDES123456";
		try {
			byte[] keyBytes = new byte[16];
			byte[] b = key.getBytes("UTF-8");
			System.arraycopy(b, 0, keyBytes, 0, keyBytes.length);
			SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
			this.ips = key.substring(0, 16);
			this.keySpec = keySpec;
		} catch (Exception e) {
			log.error("aes 실패 : {}", key.toString());
		}
	}

	/**
	 * AES128 암호화를 수행한다.
	 *
	 * @param text 암호화할 문자열
	 * @return String 암호화 된 문자열
	 * @author chauki
	 * @version 1.0
	 ** 1. 문자열 정보 입력
	 ** 2. AES encrypt 설정
	 ** 3. 암호화된 문자열 반환
	 **/
	public String encrypt(String text) {
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, keySpec,
					new IvParameterSpec(ips.getBytes()));

			byte[] encrypted = cipher.doFinal(text.getBytes("UTF-8"));
			String Str = new String(Base64.encodeBase64(encrypted));

			return Str;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * AES128 복호화를 수행한다.
	 *
	 * @param text 암호화된 문자열
	 * @return String 복호화된 문자열
	 * @author chauki
	 * @version 1.0
	 ** 1. 문자열 입력
	 ** 2. decrypt 수행
	 ** 3. 복호화된 문자열 반환
	 **/
	public String decrypt(String text) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, keySpec,
					new IvParameterSpec(ips.getBytes("UTF-8")));

			byte[] byteStr = Base64.decodeBase64(text.getBytes());
			String Str = new String(cipher.doFinal(byteStr), "UTF-8");

			return Str;
		} catch (Exception e) {
			return null;
		}
	}
}