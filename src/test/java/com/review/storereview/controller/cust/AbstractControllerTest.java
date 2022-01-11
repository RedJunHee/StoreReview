package com.review.storereview.controller.cust;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.security.config.BeanIds;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.ServletException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * Class       : AbstractControllerTest
 * Author      : 조 준 희
 * Description : Class Description
 * History     : [2022-01-11] - 조 준희 - Class Create
 */
@SpringBootTest
public abstract class AbstractControllerTest {

    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    abstract protected Object controller();
    protected MediaType contentType = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );
    @BeforeEach
    private void setup() {
        DelegatingFilterProxy delegatingFilterProxy = new DelegatingFilterProxy();
        try {
            delegatingFilterProxy.init(new MockFilterConfig(context.getServletContext(), BeanIds.SPRING_SECURITY_FILTER_CHAIN));
        } catch (ServletException e) {
            e.printStackTrace();
        }
        mockMvc = MockMvcBuilders.standaloneSetup(controller())
                // Body 데이터 한글 안깨지기 위한 인코딩 필터 설정.
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .addFilter(delegatingFilterProxy)
                .alwaysDo(print())
                .build();
    }

}