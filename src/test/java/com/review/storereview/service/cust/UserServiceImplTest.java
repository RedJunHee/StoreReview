package com.review.storereview.service.cust;

import com.review.storereview.common.enumerate.Gender;
import com.review.storereview.common.exception.PersonAlreadyExistsException;
import com.review.storereview.dto.request.UserSaveRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserServiceImpl 테스트")
class UserServiceImplTest {
//    private UserServiceImpl userService = new UserServiceImpl(userRepository);
    LocalDate birthDate = LocalDate.of(1999, 11, 15);

    @Test
    public void 유저_생성(@Mock UserServiceImpl userService) {
        // given
        UserSaveRequestDto userSaveRequestDto = UserSaveRequestDto.builder()     // hibernate: 같은 KEY값은 UPDATE
                .suid("RE001341155s").said("KA0223874453").id("moonz99").name("문").nickname("moonz").password("1234567")
                .birthDate(birthDate).gender(Gender.W).phone("01012345678")
                .build();

        doThrow(new PersonAlreadyExistsException()).when(userService).validateDuplicateUser(userSaveRequestDto.getId());
    }
/*
    @Transactional  // 테스트 실행 후 다시 Rollback 된다. (@Commit 붙여줄 경우 테스트 시 실행된 트랜잭션이 커밋되어 롤백되지않는다.)
    @Commit
    @Test
    @DisplayName("이미 있는 ID로 회원가입시 실패")
    public void 중복회원생성_예외() throws RuntimeException{
        // given
        LocalDate birthDate = LocalDate.now();
        UserSaveRequestDto userSaveRequestDto = UserSaveRequestDto.builder().suid("EE001341155s").said("test1").id("banan99").name("뭉지").nickname("moon").password("123456").birthDate(birthDate).gender(Gender.W).phone("01013572468")
                .build();
        System.out.println(userSaveRequestDto.getSuid());
        assertThrows(PersonAlreadyExistsException.class,
                () -> {
                    userService.join(userSaveRequestDto);

                    userService.join(userSaveRequestDto);
                });
    }*/
}