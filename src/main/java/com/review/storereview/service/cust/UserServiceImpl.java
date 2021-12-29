package com.review.storereview.service.cust;

import com.review.storereview.dao.cust.User;
import com.review.storereview.dto.request.UserSigninRequestDto;
import com.review.storereview.repository.cust.BaseUserRepository;
import com.review.storereview.dto.request.UserSaveRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class UserServiceImpl implements BaseUserService {

    private final BaseUserRepository userRepository;
//    private BCryptPasswordEncoder encoder;   // 암호화

    @Autowired
    public UserServiceImpl(BaseUserRepository UserRepository) {
        this.userRepository = UserRepository;
    }

    /**
     * 회원 가입 서비스
     *
     * @param userSaveRequestDto
     * @return User.suid
     */
    @Override
    public User join(UserSaveRequestDto userSaveRequestDto) throws NoSuchAlgorithmException {
        // 중복 회원 검증
        if (!validateDuplicateUser(userSaveRequestDto)) {
            return null; //에러 처리. -> User 외의 객체로 반환해야하는데..
        }
        // 인코딩
        userSaveRequestDto.passwordEncoding(encrypt(userSaveRequestDto.getPassword()));
        User result = userRepository.save(userSaveRequestDto.toEntity());

        return result; // result값이 있으면 result 반환, 아니면 other
    }

    // 중복 회원 검증
    @Override
    public boolean validateDuplicateUser(UserSaveRequestDto requestDto) {
        User findUser = userRepository.findBySuid(requestDto.getSuid());
        System.out.println(findUser.getSuid());
        if (findUser == null) return true;  // 중복 아니면
        else return false;  // 중복이면
    }

    // 인코딩 : BCryptPasswordEncoder 이용하면 될듯. 임시로 아래 예제 사용
    public static String encrypt(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");        //  java.security 이용
        byte[] passByte = password.getBytes(StandardCharsets.UTF_8);
        md.reset();
        byte[] digested = md.digest(passByte);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < digested.length; i++) {
            sb.append(Integer.toHexString(0xf & digested[i]));
        }
        return sb.toString();
    }

    @Override
    public User sign_in(UserSigninRequestDto requestDto) {
        User user = requestDto.toEntity();
         //return userRepository.findByIdAndPassword(user.getId(),user.getPassword() );

        Optional<User> result = userRepository.findByIdAndPassword(user.getId(),user.getPassword());

        if(result.isPresent())
            return result.get();
        else
            return null;

    }
}
