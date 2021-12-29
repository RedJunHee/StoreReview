package com.review.storereview.repository;

import com.review.storereview.dao.cust.User;
import com.review.storereview.common.enumerate.Gender;
import com.review.storereview.repository.cust.BaseUserRepository;
import com.review.storereview.service.cust.BaseUserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest         // @ExtendWith 가 포함되어 있음.
class BaseUserRepositoryTest {
    @Autowired
    BaseUserRepository userRepository;
    @Autowired
    BaseUserService userService;

//    @AfterEach  // Test 실행 후에 실행되는 메서드
//    public void cleanup() {       // db에서 데이터 삭제
//        userRepository.deleteAll();
//    }
    @Transactional
    @Commit
    @Test
    public void 유저생성_조회() {
        // given
//        LocalDate date = LocalDate.of(1999, 11, 15);
         LocalDateTime birthDate = LocalDateTime.now();
         // SERVICE 이용해서 회원가입

        userRepository.save(User.builder()          // 같은 KEY값을 UPDATE
                .SUID("DE001341155s")       // RE001341155s
                .SAID("KA0223874453")
                .ID("moonz99")
                .NAME("문")
                .NICKNAME("moonz")
                .PASSWORD("1234")
                .BIRTH_DATE(birthDate)
                .GENDER(Gender.W)
                .PHONE("01012345678")
                .build());
        // given
        Optional<User> result = Optional.ofNullable(userRepository.findBySuid("DE001341155s"));
        System.out.println("조회값 : " + result.get().getSuid());
        // then
        Assertions.assertThat(result.get().getSuid()).isEqualTo("DE001341155s");
    }
}