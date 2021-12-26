package com.review.StoreReview.controller.api;

import com.review.StoreReview.service.UserService;
import com.review.StoreReview.web.rest.controller.dto.UserSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserApiController {
    private final UserService userService;


    @PostMapping("/CUST/api/sign_up")
    public String save(@RequestBody UserSaveRequestDto requestDto) {
        System.out.println("UserApiController: save 호출");

        return userService.join(requestDto);
    }
}
