package com.review.storereview.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
/**
 * {@Summary en&de-crypt, 암호화 util 클래스 }
 * Author      : 문 윤 지
 * History     : [2022-01-14]
 */
@Component
public class CryptUtils {

    /** Aes 인스턴스 */
    private static Aes aes = new Aes();
    public static Aes getAES() {
        return aes;
    }


    public String getSecretKey() {
        return secretKey;
    }

    private String secretKey ;

    public CryptUtils(@Value("${aes-secret}") String secretKey) {
        this.secretKey = secretKey;
    }

    public static String Base64Encoding(String input)
    {
        return Base64.getEncoder().encodeToString( input.getBytes());
    }
    public static String Base64Decoding(String input){
        return Base64.getDecoder().decode(input.getBytes()).toString();
    }


    /** AES 암호화 지원*/
    public static class Aes {
        private final Logger logger = LoggerFactory.getLogger(Aes.class);
        /**
         * @param key
         * @param strToEncrypt
         * @return  암호화된 문자열을 바이트 배열로 반환
         * @throws Exception
         */
        public byte[] encryptToBytes(String key, String strToEncrypt) throws Exception  {
            try {
                SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                return cipher.doFinal(strToEncrypt.getBytes("UTF-8"));
            }catch(Exception e)
            {
                logger.debug("encryptToBytes Exception "+ e.getMessage());
                throw e;
            }
        }
        /** AES 복호화 지원
             * @param key
             * @param strToEncrypt
             * @return  암호화된 문자열을 바이트 배열로 반환
             * @throws Exception
             */
        public byte[] decryptToBytes(String key, String strToEncrypt) throws Exception  {
            try{
                SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
                return cipher.doFinal(strToEncrypt.getBytes("UTF-8"));
            }catch(Exception e) {
                logger.debug("decryptToBytes Exception "+ e.getMessage());
            throw e;
            }
        }
        /**
         * 문자열을 복호화
         */
        public String decrypt(String key, String strToEncrypt) throws Exception  {
            String decryptedStr = decryptToBytes(key, strToEncrypt).toString();
            return decryptedStr;
        }

        /**
         * 문자열을 암호화
         * @param key
         * @param strToEncrypt
         * @return  암호화된 문자열을 base64로 encode해서  반환
         * @throws Exception
         */
        public String encrypt(String key, String strToEncrypt) throws Exception  {
            String encryptedStr = encryptToBytes(key, strToEncrypt).toString();
            return encryptedStr;
        }


        /**
         * 문자열을 암호화
         * @param key
         * @param strToEncrypt
         * @return  암호화된 문자열을 base64로 encode해서  반환
         * @throws Exception
         */
        public String encryptAndBase64(String key, String strToEncrypt) throws Exception  {
            String encryptedStr = Base64.getEncoder().encodeToString(encryptToBytes(key, strToEncrypt));
            return encryptedStr;
        }
    }
}
