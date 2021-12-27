package com.review.StoreReview.controller.api;

import com.review.StoreReview.service.UserService;
import com.review.StoreReview.web.rest.controller.dto.UserSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserApiController {
    private final UserService userService;

    @Autowired
    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/CUST/api/sign_up")
    public String save(@RequestBody UserSaveRequestDto requestDto) {
        System.out.println("UserApiController: save 호출");

        return userService.join(requestDto);
    }
}
