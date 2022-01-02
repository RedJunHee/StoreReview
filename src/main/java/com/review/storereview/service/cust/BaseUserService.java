package com.review.storereview.service.cust;

import com.review.storereview.dao.cust.User;
import com.review.storereview.dto.request.UserSaveRequestDto;
import com.review.storereview.dto.request.UserSigninRequestDto;
import com.review.storereview.service.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.NoSuchAlgorithmException;

public interface BaseUserService extends BaseService {
    // 회원가입
    @Transactional  // 전체에서 실패하면 Rollback됨. 따로 로직을 짜줘야함.
    User join(UserSaveRequestDto requestDto) throws NoSuchAlgorithmException;

     // 중복 회원 검증
    boolean validateDuplicateUser(UserSaveRequestDto requestDto);

    // 로그인
    User sign_in(UserSigninRequestDto requestDto);
}
