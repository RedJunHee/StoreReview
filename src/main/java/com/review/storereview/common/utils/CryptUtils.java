package com.review.storereview.common.utils;

import io.jsonwebtoken.io.Decoders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
/**
 * {@Summary en&de-crypt, 암호화 util 클래스 }
 * Author      : 문 윤 지
 * History     : [2022-01-14]
 */
@Component
public class CryptUtils {
    private final Logger logger = LoggerFactory.getLogger(CryptUtils.class);

    private String secretKey ;
    static String IV = ""; // 16bit

    public CryptUtils(@Value("${aes-secret}") String secretKey) {
        this.secretKey = secretKey;
        IV = secretKey.substring(0, 16);
    }

    public String getSecretKey() {
        return secretKey;
    }

    /**
     *  문자열을 Base64로 인코딩
     * @param input
     * @return
     */
    public static String Base64Encoding(String input)
    {
        byte[] targetBytes = input.getBytes(); // Base64 인코딩 ///////////////////////////////////////////////////
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode(targetBytes);
        return new String(encodedBytes);
    }

    /**
     * Base64로 인코딩된 문자열을 Base64디코딩
     * @param input
     * @return
     */
    public static String Base64Decoding(String input){
        byte[] targetBytes = input.getBytes();
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decodedBytes = decoder.decode(targetBytes);
        return new String(decodedBytes);
    }


    /**
     * 평문을 AES256으로 암호화하여 Base64인코딩 합니다.
     * @param text 평문
     * @return 평문을 AES256으로 암호화 후 Base64인코딩한 문자열
     * @throws Exception
     */
    public String AES_Encode(String text) throws Exception{
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(IV.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);

        byte[] encrypted = cipher.doFinal(text.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encrypted);

    }

    /**
     * AES256으로 암호화된 문자열을 AES256으로 복호화하여 Base64인코딩 합니다.
     * @param cipherText 암호화된 문자열
     * @return 복호화된 평문을 Base64인코딩한 문자열
     * @throws Exception
     */
    public String AES_Decode(String cipherText) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(IV.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);

        byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
        byte[] decrypted = cipher.doFinal(decodedBytes);
        return new String(decrypted, "UTF-8");
    }
}
