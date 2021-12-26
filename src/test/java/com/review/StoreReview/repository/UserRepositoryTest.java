package com.review.StoreReview.repository;

import com.review.StoreReview.domain.CUST.User;
import org.assertj.core.api.Assertions;
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
        userRepository.deleteAll();
    }
    @Test
    public void 유저생성_조회() {
        // given
//        LocalDate date = LocalDate.of(1999, 11, 15);
         LocalDateTime birthDate = LocalDateTime.parse("1999-11-15");
        userRepository.save(User.builder()
                .SUID("RE0013411547")
                .SAID("KA0223874413")
                .ID("moonz99")
                .NAME("문윤지")
                .NICKNAME("moonz")
                .PASSWORD("1234")
                .BIRTH_DATE(birthDate)
                .GENDER('W')
                .PHONE("01012345678")
                .build());

        // when
        User user = userRepository.findBySUID("RE0013411547")    // SUID로 검색
                .orElseThrow(()-> {
                    return new NullPointerException("해당 사용자를 찾을 수 없습니다.");
                });

        //then
        Assertions.assertThat(user.getId()).isEqualTo("moonz99");
    }
}