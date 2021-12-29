package com.review.storereview.controller.cust;

import com.review.storereview.service.BaseUserService;
import com.review.storereview.dto.request.UserSaveRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserApiController {
    private final BaseUserService userService;

    @Autowired
    public UserApiController(BaseUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/CUST/api/sign_up")
    public String save(@RequestBody UserSaveRequestDto requestDto) {
        System.out.println("UserApiController: save 호출");

        return userService.join(requestDto);
    }
}