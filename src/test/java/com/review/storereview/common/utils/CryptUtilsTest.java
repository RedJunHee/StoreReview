package com.review.storereview.common.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
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

}