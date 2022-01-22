package com.review.storereview.service.cust;

import com.review.storereview.common.exception.PersonAlreadyExistsException;
import com.review.storereview.common.exception.PersonNotFoundException;
import com.review.storereview.common.utils.CryptUtils;
import com.review.storereview.dao.cust.User;
import com.review.storereview.dto.request.UserSigninRequestDto;
import com.review.storereview.repository.cust.BaseUserRepository;
import com.review.storereview.dto.request.UserSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements BaseUserService {

    private static int SUID_NUM = 0;
    private static String SUID_CHAR = "SI"; // Service Id
    private static int SAID_NUM = 0;
    private static String SAID_CHAR_RV = "RV"; // 일반 회원가입의 SAID는 "REVIEW"
    private User findUserId = null;
    private static String KEY = "12345678901234567890123456789012"; // 암호화를 위한 key (256bit , 32byte 문자열)
    private final BaseUserRepository userRepository;
    //private final BCryptPasswordEncoder passwordEncoder;     // 암호화

    /**
     * 회원 가입 서비스
     * @param userSaveRequestDto
     * @return User
     */
    @Override
    public void join(UserSaveRequestDto userSaveRequestDto)  {
        // 1. 중복 회원 검증 (id)
        validateDuplicateUserByUserId(userSaveRequestDto.getUserId());

        // 2. DB 저장
        userRepository.save(userSaveRequestDto.toEntity());
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
    public User sign_in(UserSigninRequestDto requestDto) throws Exception {
        User loginUser = requestDto.toEntity();

        Optional<User> result = userRepository.findByUserIdAndPassword(loginUser.getUserId(),loginUser.getPassword());

        // null 체크
        if(!result.isPresent()) {
            throw new PersonNotFoundException();
        }
        User user = result.get();
        // SUID, SAID 암호화(AES-256 : 암호화&복호화 가능 알고리즘)
        CryptUtils.Aes aes = CryptUtils.getAES();
        user.setSaid(aes.encrypt(KEY, user.getSaid()));
        user.setSuid(aes.encrypt(KEY, user.getSuid()));

        return user;
    }

    public User listUserIdBySuid(String suid) {
        List<Object[]> resultUserId = userRepository.findUserIdBySuid(suid);
        Object[] objects = resultUserId.get(0);
        findUserId = (User) objects[0];

        return findUserId;
    }
}
