package com.review.storereview.service.cms;

import com.review.storereview.common.exception.PersonAlreadyExistsException;
import com.review.storereview.dto.ResponseJsonObject;
import com.review.storereview.dao.cms.User;
import com.review.storereview.repository.cms.BaseUserRepository;
import com.review.storereview.dto.request.UserSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements BaseUserService {

    private static int SUID_NUM = 0;
    private static String SUID_CHAR = "SI"; // Service Id
    private static int SAID_NUM = 0;
    private static String SAID_CHAR_RV = "RV"; // 일반 회원가입의 SAID는 "REVIEW"

//    private static String KEY = "12345678901234567890123456789012"; // 암호화를 위한 key (256bit , 32byte 문자열)
    private final BaseUserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;     // 암호화
    private ResponseJsonObject resDto;
    /**
     * 회원 가입 서비스
     * @param userSaveRequestDto
     * @return User
     */
    @Override
    public boolean join(UserSaveRequestDto userSaveRequestDto)  {
        // 1. 중복 회원 검증 (id)
        validateDuplicateUserByUserId(userSaveRequestDto.getUserId());

        // 2. SUID (CHAR + 10자리 숫자-1씩 증가) 생성
        String suid = SUID_CHAR + String.format("%010d", ++SUID_NUM);
        String said = SAID_CHAR_RV + String.format("%010d", ++SAID_NUM);
        User user = userSaveRequestDto.toEntity();
        user.setSuid(suid);
        user.setSaid(said);

        // 3. 해쉬 암호화된 비밀번호 인코딩 (bcrypt)
        String encodedPwd = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPwd);

        // 4. DB 저장
        userRepository.save(user);
        System.out.println("사용자 정보 DB 저장 완료");
        return false;
    }

    // 중복 회원 검증
    @Override
    public void validateDuplicateUserByUserId(String userId) throws PersonAlreadyExistsException {
        System.out.println("validateDuplicateUser 호출됨");
        boolean isExist = userRepository.existsByUserId(userId);

        if (isExist) // 중복이면 true
            throw new PersonAlreadyExistsException();
    }
}
