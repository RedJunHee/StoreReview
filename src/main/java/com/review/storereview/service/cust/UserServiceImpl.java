package com.review.storereview.service.cust;

import com.review.storereview.repository.cust.UserRepository;
import com.review.storereview.service.BaseUserService;
import com.review.storereview.dto.request.UserSaveRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements BaseUserService {
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
