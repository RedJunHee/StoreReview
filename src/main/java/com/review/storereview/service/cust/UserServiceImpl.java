package com.review.storereview.service.cust;

import com.review.storereview.common.exception.PersonAlreadyExistsException;
import com.review.storereview.common.exception.PersonNotFoundException;
import com.review.storereview.dao.cust.User;
import com.review.storereview.dto.request.UserSigninRequestDto;
import com.review.storereview.repository.cust.BaseUserRepository;
import com.review.storereview.dto.request.UserSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements BaseUserService {

    private final BaseUserRepository userRepository;
    //private final BCryptPasswordEncoder passwordEncoder;     // 암호화

    /**
     * 회원 가입 서비스
     * @param userSaveRequestDto
     * @return User
     */
    @Override
    public User join(UserSaveRequestDto userSaveRequestDto)  {
        // 중복 회원 검증 (id)
        validateDuplicateUserByUserId(userSaveRequestDto.getUserId());

        /* 인코딩 및 PW 재설정 -> 추후 SUID, SAID 인코딩 팔요
        String encodedPW = passwordEncoder.encode(userSaveRequestDto.getPassword());
        userSaveRequestDto.setPasswordEncoding(encodedPW);
         */
        User result = userRepository.save(userSaveRequestDto.toEntity());
        System.out.println(result.getSuid());

        return result; // result값이 있으면 result 반환, 아니면 other
    }

    // 중복 회원 검증
    @Override
    public void validateDuplicateUserByUserId(String userId) {
        System.out.println("validateDuplicateUser 호출됨");
        boolean isExist = userRepository.existsByUserId(userId);
        if (isExist)  // 중복이면 true
            throw new PersonAlreadyExistsException();
    }

    /**
     * 로그인 서비스
     * @return User
     */
    @Override
    public User sign_in(UserSigninRequestDto requestDto) throws RuntimeException {
        User loginUser = requestDto.toEntity();

        Optional<User> result = userRepository.findByUserIdAndPassword(loginUser.getUserId(),loginUser.getPassword());

        // suid & 비밀번호 비교
        if(result.get().getSuid() == null) {
            throw new PersonNotFoundException();
        }

        return result.get();
    }

}
