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
    Map<String, String> errorsMap = new HashMap<>();

    @Override
    public boolean supports(Class<?> clazz) {   // 해당 클래스를 지원하는 Validator인지 확인
        return UserSaveRequestDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserSaveRequestDto userSaveRequestDto = (UserSaveRequestDto) target;
        BindingResult bindingResult = (BindingResult) errors;

        // 검증 로직
        // 1. id : null 체크
        if (ObjectUtils.isEmpty(userSaveRequestDto.getUserId())) {
            bindingResult.addError(new FieldError("UserSaveRequestDto"
            , "id"
            , "아이디를 입력하셔야합니다."));
//            errorMap.put(bindingResult.getFieldError().getField(), bindingResult.getFieldError().getDefaultMessage());
            errorsMap.put("id", "아이디를 입력하셔야합니다.");
            System.out.println(errorsMap.entrySet());
        }

        /*
        // 2. id : 패턴 체크
        String idRegex = "^[a-zA-Z]{1}[a-zA-Z0-9_]{4,11}$";   // 시작은 영문으로만, '_'를 제외한 특수문자 x, 영문, 숫자, '_'으로만 이루어진 5 ~ 12자 이하
        if(Pattern.matches(idRegex, userSaveRequestDto.getId()))
            errors.put();

        // 3. password : 패턴 체크

        // 4. phone : 패턴 체크
        String phoneRegex = "^\\d{2,3}-\\d{3,4}-\\d{4}$";
        if (Pattern.matches(phoneRegex, userSaveRequestDto.getPhone()))

        // + 추가 검증 필요시 추가..
         */
    }

    public Map<String, String> getErrorsMap() {
        return errorsMap;
    }
}
