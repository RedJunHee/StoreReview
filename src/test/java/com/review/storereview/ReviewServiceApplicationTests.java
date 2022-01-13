package com.review.storereview;

import com.review.storereview.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class ReviewServiceApplicationTests {

	AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SecurityConfig.class); // 등록된 bean의 이름을 get
	@Test
	void contextLoads() throws Exception{
		String[] beanNames = ac.getBeanDefinitionNames();          // bean 이름을 출력
		for(String beanName : beanNames) {             System.out.println(beanName);         }
	}

	@Test
	@DisplayName("SHA-256암호화 테스트")
	void sha256_테스트()
	{
		String password = "hi jjh!!";
		try {
			MessageDigest sha = MessageDigest.getInstance("SHA-256");
			sha.update(password.getBytes());

			System.out.println(bytesToHex(sha.digest()));

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	private String bytesToHex(byte[] bytes) {
		StringBuilder builder = new StringBuilder();
		for (byte b : bytes) {
			builder.append(String.format("%02x", b));
		}
		return builder.toString();
	}
}
