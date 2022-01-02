package com.review.storereview.controller.cust;

import com.review.storereview.common.enumerate.ApiStatusCode;
import com.review.storereview.dao.cust.User;
import com.review.storereview.dto.ResponseJsonObject;
import com.review.storereview.dto.request.UserSigninRequestDto;
import com.review.storereview.dto.response.UserResponseDto;
import com.review.storereview.service.cust.BaseUserService;
import com.review.storereview.dto.request.UserSaveRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public ResponseEntity<ResponseJsonObject> save(@RequestBody UserSaveRequestDto userSaveRequestDto) throws NoSuchAlgorithmException {
        System.out.println("UserApiController: save 호출");
        // ResponseJsonOBject 사용
        ResponseJsonObject resDto = null;
        UserResponseDto responseDto;
        User user = userService.join(userSaveRequestDto);

        resDto = ResponseJsonObject.builder().withMeta(
                ResponseJsonObject.Meta.builder()
                        .withCode(ApiStatusCode.OK)
                        .build()).build();  // Data는 null

        return new ResponseEntity<ResponseJsonObject>(resDto, HttpStatus.OK);
    }

    @PutMapping(value = "/api/sign_in")
    public ResponseEntity<ResponseJsonObject> sign_in(@RequestBody UserSigninRequestDto requstDto)
    {
        ResponseJsonObject resDto = null;
        UserResponseDto responseDto;
        User user = userService.sign_in(requstDto);

        responseDto = UserResponseDto.builder()
                .suid(user.getSuid())
                .said(user.getSaid())
                .birthDate(user.getBirthDate())
                .id(user.getId())
                .gender(user.getGender())
                .name(user.getName())
                .nickname(user.getNickname())
                .phone(user.getPhone())
                .build();

       resDto = ResponseJsonObject.builder().withMeta(
                ResponseJsonObject.Meta.builder()
                        .withCode(ApiStatusCode.OK)
                        .build()).withData( responseDto).build();

        return new ResponseEntity<ResponseJsonObject>(resDto, HttpStatus.OK);
    }
}
