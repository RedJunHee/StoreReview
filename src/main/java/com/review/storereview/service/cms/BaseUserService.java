package com.review.storereview.service.cms;

import com.review.storereview.dto.request.UserSaveRequestDto;
import com.review.storereview.service.BaseService;
import org.springframework.transaction.annotation.Transactional;
import java.security.NoSuchAlgorithmException;

public interface BaseUserService extends BaseService {
    // 회원가입
    @Transactional  // 전체에서 실패하면 Rollback됨. 따로 로직을 짜줘야함.
    boolean join(UserSaveRequestDto requestDto) throws NoSuchAlgorithmException;

    void validateDuplicateUserByUserId(String userId);
}
