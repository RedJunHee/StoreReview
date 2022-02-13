package com.review.storereview.controller.cms;

import com.review.storereview.common.enumerate.ApiStatusCode;
import com.review.storereview.dto.ResponseJsonObject;
import com.review.storereview.dto.validator.UserSaveDtoValidator;
import com.review.storereview.service.cms.BaseUserService;
import com.review.storereview.dto.request.UserSaveRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
public class UserApiController {
    private final BaseUserService userService;
//    private UserSaveDtoValidator userSaveDtoValidator;
    @Autowired
    public UserApiController(BaseUserService userService) {
        this.userService = userService;
//        this.userSaveDtoValidator = new UserSaveDtoValidator();
    }

    /**
     * 회원가입 요청 처리 api
     * @param userSaveRequestDto
     * @return ResponseEntity<ResponseJsonObject>
     */
    @PostMapping("/api/signup")
    public ResponseEntity<ResponseJsonObject> save(@RequestBody UserSaveRequestDto userSaveRequestDto) throws NoSuchAlgorithmException {
        System.out.println("UserApiController: save 호출");
        // 1. 파라미터 검증
        // 1-1. 검증 실패 로직
        Map<String, String> validatorResult =  checkParameterValid(userSaveRequestDto);
        if (!validatorResult.isEmpty()) {
            ResponseJsonObject exceptionDto = ResponseJsonObject.withParameterMsg(
                    ApiStatusCode.PARAMETER_CHECK_FAILED.getCode(),
                    ApiStatusCode.PARAMETER_CHECK_FAILED.getType(),
                    ApiStatusCode.PARAMETER_CHECK_FAILED.getMessage(),
                    validatorResult);
            return new ResponseEntity<>(exceptionDto, HttpStatus.BAD_REQUEST);
        }
        else {      // 1-2. 검증 성공 로직
            // 2. join 서비스 로직
            userService.join(userSaveRequestDto);

            // 3. responseDto 생성
            ResponseJsonObject resDto = ResponseJsonObject.withStatusCode(ApiStatusCode.OK.getCode());
            return new ResponseEntity<>(resDto, HttpStatus.OK);
        }
    }

    @PutMapping(value = "/api/test")
    public ResponseEntity<ResponseJsonObject> test()
    {
        ResponseJsonObject resDto = null;

        resDto = ResponseJsonObject.withStatusCode(ApiStatusCode.OK.getCode()).setData("DATA!!");

        // 5.
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }
    /**
     * 바인딩된 UserSaveRequestDto를 검증한다.
     * @param requestDto
     */
    private Map<String, String> checkParameterValid(UserSaveRequestDto requestDto) {
        Map<String, String> errorsMap = new HashMap<>();
        String defaultMessage;

        // 1. id : null 체크
        if (ObjectUtils.isEmpty(requestDto.getUserId())) {
            defaultMessage = "아이디를 입력해야 합니다.";
            errorsMap.put("valid_id_null", defaultMessage);
        }
        // 2. id : 이메일 패턴 체크
        String emailRegex = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
        if(!ObjectUtils.isEmpty(requestDto.getUserId()) && !Pattern.matches(emailRegex, requestDto.getUserId())) {
            defaultMessage = "유효한 이메일을 작성해야 합니다.";
            errorsMap.put("valid_id_pattern", defaultMessage);
        }
        // 3. 비밀번호 null 체크
        if (ObjectUtils.isEmpty(requestDto.getPassword())) {
            defaultMessage = "비밀번호를 입력해야 합니다.";
            errorsMap.put("valid_password", defaultMessage);
        }
        // 4. 핸드폰 번호 체크
        String phoneRegex = "^\\d{3}\\d{3,4}\\d{4}$";
        if(!Pattern.matches(phoneRegex, requestDto.getPhone())) {
            defaultMessage = "핸드폰 번호를 알맞게 작성해야 합니다. ex) 01012345678";
            errorsMap.put("valid_phone", defaultMessage);
        }
        return errorsMap;
    }
}
