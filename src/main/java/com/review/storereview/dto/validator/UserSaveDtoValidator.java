package com.review.storereview.dto.validator;

import com.review.storereview.dto.request.UserSaveRequestDto;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/** Class       : UserSaveDtoValidator (DTO)
 *  Author      : 문 윤 지
 *  Description : 회원가입 시 파라미터에 대한 Custom Validator
 *  History     : [2021-01-11]
 */
public class UserSaveDtoValidator implements Validator {
    // 검증 오류 결과 보관
    private static Map<String, String> errorsMap = new HashMap<>();

    @Override
    public boolean supports(Class<?> clazz) {   // 해당 클래스를 지원하는 Validator인지 확인
        return UserSaveRequestDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserSaveRequestDto userSaveRequestDto = (UserSaveRequestDto) target;
        BindingResult bindingResult = (BindingResult) errors;
        String defaultMessage;

        // 검증 로직
        // 1. id : null 체크
        if (ObjectUtils.isEmpty(userSaveRequestDto.getUserId())) {
            defaultMessage = "아이디를 입력해야 합니다.";
            bindingResult.addError(new FieldError("UserSaveRequestDto"
            , "id"
            , defaultMessage));
            errorsMap.put("id", defaultMessage);
        }

        // 2. id : 이메일 패턴 체크
        // 패턴 : 영문으로 시작, 영문+숫자+'_','.','-'로 이루어진 5~12자 사이 이메일
        String emailRegex = "^[a-zA-Z]{1}[a-zA-Z0-9+_.-]+@(.+)$";
        if(!Pattern.matches(emailRegex, userSaveRequestDto.getUserId())) {
            defaultMessage = "유효한 이메일을 작성해야 합니다.";
            bindingResult.addError(new FieldError("UserSaveRequestDto"
                    , "id"
                    , defaultMessage));
            errorsMap.put("id",defaultMessage);
        }

        // 3. password : 패턴 체크
        // 패턴 : 영문, 숫자, 특수문자 포함된 비밀번호
        String pwdRegex = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}$";
        if(!Pattern.matches(pwdRegex, userSaveRequestDto.getPassword())) {
            defaultMessage = "비밀번호는 영문, 숫자, 특수문자를 포함해야 하며 8자와 20자 사이로 작성해야 합니다.";
            bindingResult.addError(new FieldError("UserSaveRequestDto"
                    , "password"
                    , defaultMessage));
            errorsMap.put("password",defaultMessage);
        }
    }

    public Map<String, String> getErrorsMap() {
        return errorsMap;
    }
}
