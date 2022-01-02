package com.review.storereview.common;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WebSecurityConfigTest {
    @Autowired @Qualifier("BCryptPasswordEncoder")
    BCryptPasswordEncoder passwordEncoder;
//    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // 인스턴스 생성

    @Test
    @DisplayName("패스워드 암호화 테스트")
    void getPasswordEncoder() {
        // given
        String rawPW = "123456789";
        // when
        String encodedPW = passwordEncoder.encode(rawPW);
        // then
        Assertions.assertThat(rawPW).isNotEqualTo(encodedPW);
        assertTrue(passwordEncoder.matches(rawPW, encodedPW));
    }
}