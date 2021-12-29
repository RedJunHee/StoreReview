package com.review.storereview.service.cust;

import com.review.storereview.dto.request.UserSaveRequestDto;
import com.review.storereview.service.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;

@Service
public interface BaseUserService extends BaseService {
    // 회원가입
    @Transactional  // 전체에서 실패하면 Rollback됨. 따로 로직을 짜줘야함.
    String join(UserSaveRequestDto requestDto) throws NoSuchAlgorithmException;

     // 중복 회원 검증
    boolean validateDuplicateUser(UserSaveRequestDto requestDto);
}
