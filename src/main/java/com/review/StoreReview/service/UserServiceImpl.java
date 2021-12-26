package com.review.StoreReview.service;

import com.review.StoreReview.repository.UserRepository;
import com.review.StoreReview.web.rest.controller.dto.UserSaveRequestDto;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String join(UserSaveRequestDto requestDto) {
        // 회원가입 로직
        return userRepository.save(requestDto.toEntity()).getSuid();
    }
}
