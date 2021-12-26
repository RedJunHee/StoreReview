package com.review.StoreReview.service;

import com.review.StoreReview.web.rest.controller.dto.UserSaveRequestDto;

public interface UserService {
    // 회원가입
    String join(UserSaveRequestDto requestDto);
}
