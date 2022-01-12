package com.review.storereview.common.exception.handler;

import com.review.storereview.common.enumerate.Gender;
import com.review.storereview.common.exception.PersonAlreadyExistsException;
import com.review.storereview.dto.request.UserSaveRequestDto;
import com.review.storereview.repository.cust.BaseUserRepository;
import com.review.storereview.service.cust.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@DisplayName("GlobalExceptionHandler 테스트")
class GlobalExceptionHandlerTest {
}