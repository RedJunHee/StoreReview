package com.review.storereview.controller.cms;

import com.review.storereview.service.cms.ReviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.security.config.BeanIds;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.ServletException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * {@Summary Review Api Controller 테스트 코드 }
 * Class       : ReviewApiControllerTest
 * Author      : 문 윤 지
 * History     : [2022-01-23]
 */
@WebMvcTest
class ReviewApiControllerTest  {
    @Autowired
    protected MockMvc reviewControllerMockMvc;

    @MockBean
    private ReviewServiceImpl reviewService;
    List<String> imgUrl = new ArrayList<String>(Arrays.asList("http://s3-img-url-test1.com","http://s3-img-url-test2.com"));
    Integer stars = 4;

    private ReviewApiController reviewController;

    protected Object reviewController() {
        return reviewController;
    }
    protected MediaType contentType = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setUp() {   // UserApiController를 MockMvc 객체로 만든다.
        reviewControllerMockMvc = MockMvcBuilders.standaloneSetup(new ReviewApiController(reviewService))
                .addFilters(new CharacterEncodingFilter("UTF-8", true)) // charset을 UTF-8로 설정 (option)
                .build();
        DelegatingFilterProxy delegatingFilterProxy = new DelegatingFilterProxy();
        try {
            delegatingFilterProxy.init(new MockFilterConfig(context.getServletContext(), BeanIds.SPRING_SECURITY_FILTER_CHAIN));
        } catch (ServletException e) {
            e.printStackTrace();
        }
        reviewControllerMockMvc = MockMvcBuilders.standaloneSetup(reviewController())
                // Body 데이터 한글 안깨지기 위한 인코딩 필터 설정.
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .addFilter(delegatingFilterProxy)
                .alwaysDo(print())
                .build();
    }
/*    @Test
    @DisplayName("리뷰 등록 확인")
    void uploadReview() throws Exception {
        ReviewUploadRequestDto requestDto = new ReviewUploadRequestDto("1234", "테스트 업로드", stars, imgUrl);
         reviewControllerMockMvc.perform(post("/review")
                         .contentType(contentType)
                         .content(new ObjectMapper().writeValueAsString(requestDto)))
                 .andDo(print())
                .andExpect(status().isOk())      // HttpStatus.OK(200)
                .andExpect(content().contentType("application/json;charset=utf-8"))     // contentType 검증
                .andExpect(jsonPath("$.meta.code").value(200))
//                .andExpect(jsonPath("$.data").value())
        ;
    }*/

    @Test
    @DisplayName("리뷰 조회 확인")
    void findReview() throws Exception {
        // 리뷰 1개 조회
/*        Long reviewId = 1L;
        reviewControllerMockMvc.perform(get("/reviews/{" + reviewId + "}")
                        .contentType(contentType))
                .andDo(print())
                .andExpect(status().isOk())      // HttpStatus.OK(200)
                .andExpect(content().contentType("application/json;charset=utf-8"))     // contentType 검증
                .andExpect(jsonPath("$.meta.code").value(200))
//                .andExpect(jsonPath("$.data").value())
        ;*/

        // 리뷰 n개 조회
        String placeId = "1234";
        reviewControllerMockMvc.perform(get("/places/{" + placeId + "}")
                        .contentType(contentType)
                        .header("Authenticate", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJteWo5OUBuYXZlci5jb20iLCJhdXRoIjoiUk9MRV9VU0VSIiwic3VpZCI6IlNJMDAwMDAwMDAwMSIsInNhaWQiOiJSVjAwMDAwMDAwMDEiLCJleHAiOjE2NDM1OTYyMTF9.ehvEo-CGIBp9Jo1XJi4KBvBcCLpEfuuP_SSBbCMXIdPL4s1_6qpPtqIhid8N3N4xM8TfL8QcG3A4_CJG2HbBCQ")
                )
                .andDo(print())
                .andExpect(status().isOk())      // HttpStatus.OK(200)
                .andExpect(content().contentType("application/json;charset=utf-8"))     // contentType 검증
                .andExpect(jsonPath("$.meta.code").value(200))
//                .andExpect(jsonPath("$.data").value())
        ;
    }

/*
    @Test
    @DisplayName("리뷰 수정 확인")
    void updateReview() throws Exception {
        Long reviewId = 1L;
        List<String> updatedImgUrl = new ArrayList<String>(Arrays.asList("http://s3-img-url-test1.com"));
        Integer stars = 3;

        ReviewUpdateRequestDto requestDto = new ReviewUpdateRequestDto("수정된 리뷰 테스트", updatedImgUrl, stars);
        reviewControllerMockMvc.perform(put("/reviews/{" + reviewId + "}")
                        .contentType(contentType)
                        .content(new ObjectMapper().writeValueAsString(requestDto))
                )
                .andDo(print())
                .andExpect(status().isOk())      // HttpStatus.OK(200)
                .andExpect(content().contentType("application/json;charset=utf-8"))     // contentType 검증
                .andExpect(jsonPath("$.meta.code").value(200))
//                .andExpect(jsonPath("$.data").value())
        ;
    }

    @Test
    @DisplayName("리뷰 삭제 확인")
    void deleteReview() throws Exception {
        Long reviewId = 1L;
        reviewControllerMockMvc.perform(delete("/reviews/{" + reviewId + "}")
                        .contentType(contentType)
                )
                .andDo(print())
                .andExpect(status().isOk())      // HttpStatus.OK(200)
                .andExpect(content().contentType("application/json;charset=utf-8"))     // contentType 검증
                .andExpect(jsonPath("$.meta.code").value(200))
                .andExpect(jsonPath("$.data").isEmpty())
        ;
    }*/
}