package com.review.storereview.dto.validator;

import com.review.storereview.dto.request.UserSaveRequestDto;
import org.springframework.stereotype.Component;
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
//@Component
public class UserSaveDtoValidator implements Validator {
    // 검증 오류 결과 보관
    private static Map<String, String> errorsMap;
    private static final String OBJECT_NAME = "UserSaveRequestDto";
    public Map<String, String> getErrorsMap() {
        return errorsMap;
    }
    @Override
    public boolean supports(Class<?> clazz) {   // 해당 클래스를 지원하는 Validator인지 확인
        return UserSaveRequestDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        errorsMap = new HashMap<>();
        UserSaveRequestDto userSaveRequestDto = (UserSaveRequestDto) target;
        BindingResult bindingResult = (BindingResult) errors;
        String defaultMessage;

        // 검증 로직
        // 1. id : null 체크
        if (ObjectUtils.isEmpty(userSaveRequestDto.getUserId())) {
            defaultMessage = "아이디를 입력해야 합니다.";
            saveErrorInfo(bindingResult, "id", defaultMessage);
        }

        // 2. id : 이메일 패턴 체크
        String emailRegex = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
        if(!ObjectUtils.isEmpty(userSaveRequestDto.getUserId()) && !Pattern.matches(emailRegex, userSaveRequestDto.getUserId())) {
            defaultMessage = "유효한 이메일을 작성해야 합니다.";
            saveErrorInfo(bindingResult, "id", defaultMessage);
        }
        // 3. 비밀번호 null 체크
        if (ObjectUtils.isEmpty(userSaveRequestDto.getPassword())) {
            defaultMessage = "비밀번호를 입력해야 합니다.";
            saveErrorInfo(bindingResult, "password", defaultMessage);
        }
        // 4. 핸드폰 번호 체크
        String phoneRegex = "^\\d{3}\\d{3,4}\\d{4}$";
        if(!Pattern.matches(phoneRegex, userSaveRequestDto.getPhone())) {
            defaultMessage = "핸드폰 번호를 알맞게 작성해야 합니다. ex) 01012345678";
            saveErrorInfo(bindingResult, "phone", defaultMessage);
        }
    }
    /**
     *  파라미터에 대한 에러 정보를 세팅한다.
     * @param bindingResult
     * @param fieldName
     * @param errorMessage
     */
    private void saveErrorInfo(BindingResult bindingResult, String fieldName, String errorMessage) {
        bindingResult.addError(
                new FieldError(OBJECT_NAME, fieldName, errorMessage));
        errorsMap.put(fieldName, errorMessage);
    }
}
