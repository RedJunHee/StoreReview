package com.review.storereview.common.utils;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Base64; import java.util.Base64.Decoder; import java.util.Base64.Encoder;

import java.nio.charset.StandardCharsets;

class CryptUtilsTest {

    CryptUtils cryptUtils;

    @Test
    @DisplayName("AES 256bit 암호화 테스트")
    public void AESEncryptTest()
    {
        String str = "암호화 테스트";
        String key = "184EBFA87C052FB66887177B429201CE";
        String result = "";
        try {
            result = cryptUtils.getAES().encrypt(cryptUtils.getSecretKey(),str);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(result);
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