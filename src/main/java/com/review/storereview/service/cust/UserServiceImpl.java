package com.review.storereview.service.cust;

import com.review.storereview.dao.cust.User;
import com.review.storereview.dto.request.UserSigninRequestDto;
import com.review.storereview.repository.cust.UserRepository;
import com.review.storereview.service.BaseUserService;
import com.review.storereview.dto.request.UserSaveRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    @Override
    public User sign_in(UserSigninRequestDto requestDto) {
        User user = requestDto.toEntity();
         //return userRepository.findByIdAndPassword(user.getId(),user.getPassword() );

        Optional<User> result = userRepository.findByIdAndPassword(user.getId(),user.getPassword());

        if(result.isPresent())
            return result.get();
        else
            return null;

    }
}
