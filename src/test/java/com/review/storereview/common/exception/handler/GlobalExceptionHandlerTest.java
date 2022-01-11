package com.review.storereview.common.exception.handler;

import com.review.storereview.common.enumerate.Gender;
import com.review.storereview.common.exception.PersonAlreadyExistsException;
import com.review.storereview.dto.request.UserSaveRequestDto;
import com.review.storereview.service.cust.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {
    @Autowired
    private UserServiceImpl userService;

    @Test
    @DisplayName("이미 있는 ID로 회원가입시 실패")
    public void 중복회원생성_예외() throws RuntimeException{
        // given
        LocalDate birthDate = LocalDate.now();
        UserSaveRequestDto userDto = UserSaveRequestDto.builder().suid("EE001341155s").said("test1").id("banan99").name("뭉지").nickname("moon").password("123456").birthDate(birthDate).gender(Gender.W).phone("01013572468")
                .build();
        System.out.println(userDto.getSuid());
        assertThrows(PersonAlreadyExistsException.class,
                () -> {
                    userService.join(userDto);

                    userService.join(userDto);
                });
//        assertEquals("Person already exists", type);
    }
}