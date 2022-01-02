package com.review.storereview.service.cust;

import com.review.storereview.dao.cust.User;
import com.review.storereview.dto.request.UserSigninRequestDto;
import com.review.storereview.repository.cust.BaseUserRepository;
import com.review.storereview.dto.request.UserSaveRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.security.NoSuchAlgorithmException;

@Service
public class UserServiceImpl implements BaseUserService {

    private final BaseUserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;     // 암호화

    @Autowired
    public UserServiceImpl(BaseUserRepository UserRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = UserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 회원 가입 서비스
     * @param userSaveRequestDto
     * @return User
     */
    @Override
    public User join(UserSaveRequestDto userSaveRequestDto) throws NoSuchAlgorithmException {
        // 중복 회원 검증
        if (!validateDuplicateUser(userSaveRequestDto)) {
            return null; //에러 처리. -> User 외의 객체로 반환해야하는데..
        }
        /* 인코딩 및 PW 재설정 -> 추후 SUID, SAID 인코딩 팔요
        String encodedPW = passwordEncoder.encode(userSaveRequestDto.getPassword());
        userSaveRequestDto.setPasswordEncoding(encodedPW);
         */
        User result = userRepository.save(userSaveRequestDto.toEntity());

        return result; // result값이 있으면 result 반환, 아니면 other
    }

    // 중복 회원 검증
    @Override
    public boolean validateDuplicateUser(UserSaveRequestDto userSaveRequestDto) {
        User findUser = userRepository.findBySuid(userSaveRequestDto.getSuid());
        System.out.println(findUser.getSuid());
        if (findUser == null) return true;  // 중복 아니면
        else return false;  // 중복이면
    }

    /**
     * 로그인 서비스
     * @param requestDto
     * @return User
     */
    @Override
    public User sign_in(UserSigninRequestDto requestDto) {
        User loginUser = requestDto.toEntity();
         //return userRepository.findByIdAndPassword(user.getId(),user.getPassword() );

        Optional<User> result = userRepository.findByIdAndPassword(loginUser.getId(),loginUser.getPassword());

        // suid & 비밀번호 비교
        if(result.get().getSuid()==null) {
            // System.out.println("해당 이메일의 유저가 존재하지 않습니다.");
            return null; // return false;
        }
        if(!passwordEncoder.matches(result.get().getPassword(),loginUser.getPassword())) {
//            System.out.println("비밀번호가 일치하지 않습니다.");
            return null;    // return false;
        }
        return result.get();
    }

}
