package com.review.storereview.service;

import com.review.storereview.dao.cust.User;
import com.review.storereview.dto.request.UserSaveRequestDto;
import com.review.storereview.dto.request.UserSigninRequestDto;

public interface BaseUserService extends BaseService {
    // 회원가입
    String join(UserSaveRequestDto requestDto);
    // 회원가입
    User sign_in(UserSigninRequestDto requestDto);
}
