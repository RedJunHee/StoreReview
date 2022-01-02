package com.review.storereview;

import com.review.storereview.common.WebSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootTest
class ReviewServiceApplicationTests {

	@Autowired
	AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(WebSecurityConfig.class); // 등록된 bean의 이름을 get
	@Test
	void contextLoads() throws Exception{
		String[] beanNames = ac.getBeanDefinitionNames();          // bean 이름을 출력
		for(String beanName : beanNames) {             System.out.println(beanName);         }
	}
}
