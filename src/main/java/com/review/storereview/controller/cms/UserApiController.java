package com.review.storereview.controller.cms;

import com.review.storereview.common.enumerate.ApiStatusCode;
import com.review.storereview.common.exception.ParamValidationException;
import com.review.storereview.dto.ResponseJsonObject;
import com.review.storereview.dto.validator.UserSaveDtoValidator;
import com.review.storereview.service.cms.BaseUserService;
import com.review.storereview.dto.request.UserSaveRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.security.NoSuchAlgorithmException;

@RestController
public class UserApiController {
    private final BaseUserService userService;
    private final UserSaveDtoValidator userSaveDtoValidator;

    @Autowired
    public UserApiController(BaseUserService userService) {
        this.userService = userService;
        this.userSaveDtoValidator = new UserSaveDtoValidator();
    }

    /**
     * 회원가입 요청 처리 api
     * @param userSaveRequestDto
     * @return ResponseEntity<ResponseJsonObject>
     */
    @PostMapping("/api/signup")
    public ResponseEntity<ResponseJsonObject> save(@RequestBody UserSaveRequestDto userSaveRequestDto, BindingResult bindingResult) throws NoSuchAlgorithmException {
        System.out.println("UserApiController: save 호출");
        ResponseJsonObject resDto = null;   // ResponseJsonOBject 사용
        // 1. 파라미터 검증
        userSaveDtoValidator.validate(userSaveRequestDto, bindingResult);

        // 1-1. 검증 실패 로직
        if (bindingResult.hasErrors()) {
            System.out.println(userSaveDtoValidator.getErrorsMap());
            ResponseJsonObject exceptionDto = new ParamValidationException(userSaveDtoValidator.getErrorsMap()).getResponseJsonObject();
//            throw exceptionDto;  // exceptionHandler에서 Controller 단에서 발생하는 예외를 잡아줌
            return new ResponseEntity<>(exceptionDto, HttpStatus.BAD_REQUEST);
        }
        else {      // 1-2. 검증 성공 로직
            // 2. join 서비스 로직
            userService.join(userSaveRequestDto);

            // 3. responseDto 생성
            resDto = ResponseJsonObject.withStatusCode(ApiStatusCode.OK);
            return new ResponseEntity<>(resDto, HttpStatus.OK);
        }
    }

    @PutMapping(value = "/api/test")
    public ResponseEntity<ResponseJsonObject> test()
    {
        ResponseJsonObject resDto = null;

        resDto = ResponseJsonObject.withStatusCode(ApiStatusCode.OK).setData("DATA!!");

        // 5.
        return new ResponseEntity<ResponseJsonObject>(resDto, HttpStatus.OK);
    }
}
