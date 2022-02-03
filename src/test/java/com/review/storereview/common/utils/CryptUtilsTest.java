package com.review.storereview.common.utils;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64; import java.util.Base64.Decoder; import java.util.Base64.Encoder;

import java.nio.charset.StandardCharsets;

class CryptUtilsTest {

    CryptUtils cryptUtils;
    public static String alg = "AES/CBC/PKCS5Padding";
    private String key = "";
    private String iv ;// 16byte

    @BeforeEach
    public void befor()
    {
        cryptUtils = new CryptUtils("184EBFA87C052FB66887177B429201CE");
        this.key = cryptUtils.getSecretKey();
        this.iv = key.substring(0, 16);
    }

    @Test
    @DisplayName("AES256 암호화")
    public void AES_Encryption(){
        String str = "암호화 테스트";
        try {
            System.out.println(cryptUtils.AES_Encode(str));
        }catch(Exception ex)
        {
            ex.getMessage();
        }
    }

    @Test
    @DisplayName("AES256 복호화")
    public void AES_Decryption(){
        String str = "yFbxF4IPEYjCpXhXzwPGIRkqlwQM0sCIFMK1sYq6oLw=";
        try {
            System.out.println(cryptUtils.AES_Decode(str));
        }catch(Exception ex)
        {
            ex.getMessage();
        }
    }


    @Test
    @DisplayName("Base64 인코딩 테스트")
    public void Base64Encoding(){
        String text = "안녕하세요";
        byte[] targetBytes = text.getBytes(); // Base64 인코딩 ///////////////////////////////////////////////////
         Encoder encoder = Base64.getEncoder();
         byte[] encodedBytes = encoder.encode(targetBytes);
         // Base64 디코딩 ///////////////////////////////////////////////////
         Decoder decoder = Base64.getDecoder();
         byte[] decodedBytes = decoder.decode(encodedBytes);
         System.out.println("인코딩 전 : " + text);
         System.out.println("인코딩 text : " + new String(encodedBytes));
         System.out.println("디코딩 text : " + new String(decodedBytes));

        System.out.println(CryptUtils.Base64Encoding(text));

        String encoText = CryptUtils.Base64Encoding(text);

        System.out.println(CryptUtils.Base64Decoding(encoText));

    }

    @Test
    @DisplayName("Base64 디코딩 테스트")
    public void Base64Decoding(){
        String string = "7JWI64WV7ZWY7IS47JqU";
        System.out.println(Decoders.BASE64.decode(string));
        System.out.println(CryptUtils.Base64Decoding(string));

    }

}