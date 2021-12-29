package com.review.storereview.repository;

import com.review.storereview.dao.cust.User;
import com.review.storereview.common.enumerate.Gender;
import com.review.storereview.repository.impl.cust.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;

@SpringBootTest         // @ExtendWith 가 포함되어 있음.
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @AfterEach  // Test 실행 후에 실행되는 메서드
    public void cleanup() {
        //userRepository.deleteAll();
    }
    @Test
    public void 유저생성_조회() {
        // given
//        LocalDate date = LocalDate.of(1999, 11, 15);
         LocalDateTime birthDate = LocalDateTime.now();
        userRepository.save(User.builder()
                .SUID("RE001341155s")
                .SAID("KA0223874453")
                .ID("moonz99")
                .NAME("문윤지")
                .NICKNAME("moonz")
                .PASSWORD("1234")
                .BIRTH_DATE(birthDate)
                .GENDER(Gender.W)
                .PHONE("01012345678")
                .build());


    }
}