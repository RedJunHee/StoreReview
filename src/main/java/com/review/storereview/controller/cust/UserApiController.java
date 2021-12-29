package com.review.storereview.controller.cust;

import com.review.storereview.service.cust.BaseUserService;
import com.review.storereview.dto.request.UserSaveRequestDto;
import com.review.storereview.service.cust.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
public class UserApiController {
    private final BaseUserService userService;

    @Autowired
    public UserApiController(BaseUserService userService) {
        this.userService = userService;
    }

    /**
     * 회원가입 요청 처리 api
     * @param userSaveRequestDto
     * @return User.suid
     */
    @PostMapping("/user/signup")
    public String save(@RequestBody UserSaveRequestDto userSaveRequestDto) throws NoSuchAlgorithmException {
        System.out.println("UserApiController: save 호출");

        return userService.join(userSaveRequestDto);
    }
}
