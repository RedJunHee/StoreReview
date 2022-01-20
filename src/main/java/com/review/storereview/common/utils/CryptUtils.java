package com.review.storereview.common.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class CryptUtils {
    /** Aes 인스턴스 */
    private static Aes aes = new Aes();

    public static Aes getAES() {
        return aes;
    }

    /** AES 암호화 지원*/
    public static class Aes {
        /**
         * @param key
         * @param strToEncrypt
         * @return  암호화된 문자열을 바이트 배열로 반환
         * @throws Exception
         */
        public byte[] encryptToBytes(String key, String strToEncrypt) throws Exception  {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher.doFinal(strToEncrypt.getBytes("UTF-8"));
        }

        /**
         * 문자열을 암호화
         * @param key
         * @param strToEncrypt
         * @return  암호화된 문자열을 base64로 encode해서  반환
         * @throws Exception
         */
        public String encrypt(String key, String strToEncrypt) throws Exception  {
            String encryptedStr = Base64.getEncoder().encodeToString(encryptToBytes(key, strToEncrypt));
            return encryptedStr;
        }
    }
}
