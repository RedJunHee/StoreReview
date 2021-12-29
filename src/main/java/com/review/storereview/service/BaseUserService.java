package com.review.storereview.service;

import com.review.storereview.dto.request.UserSaveRequestDto;

public interface BaseUserService extends BaseService {
    // 회원가입
    String join(UserSaveRequestDto requestDto);
}
