package com.review.storereview.controller.cms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.review.storereview.dto.request.ReviewUpdateRequestDto;
import com.review.storereview.dto.request.ReviewUploadRequestDto;
import com.review.storereview.service.cms.ReviewServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

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
class ReviewApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewServiceImpl reviewService;
    List<String> imgUrl = new ArrayList<String>(Arrays.asList("http://s3-img-url-test1.com","http://s3-img-url-test2.com"));
    Integer stars = 4;

    @Test
    @DisplayName("리뷰 등록 확인")
    void uploadReview() throws Exception {
        ReviewUploadRequestDto requestDto = new ReviewUploadRequestDto("1234", "테스트 업로드", stars, imgUrl);
        final ResultActions actions =
                mockMvc.perform(post("/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                    .content(new ObjectMapper().writeValueAsString(requestDto))
                );
        actions
                .andDo(print())
                .andExpect(status().isOk())      // HttpStatus.OK(200)
                .andExpect(content().contentType("application/json;charset=utf-8"))     // contentType 검증
                .andExpect(jsonPath("$.meta.code").value(200))
//                .andExpect(jsonPath("$.data").value())
        ;
    }

    @Test
    @DisplayName("리뷰 조회 확인")
    void findReview() throws Exception {
        // 리뷰 1개 조회
        Long reviewId = 1L;
        final ResultActions actions =
                mockMvc.perform(get("/reviews/{" + reviewId + "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                );
        actions
                .andDo(print())
                .andExpect(status().isOk())      // HttpStatus.OK(200)
                .andExpect(content().contentType("application/json;charset=utf-8"))     // contentType 검증
                .andExpect(jsonPath("$.meta.code").value(200))
//                .andExpect(jsonPath("$.data").value())
        ;

        // 리뷰 n개 조회
        String placeId = "1234";
        final ResultActions findActions =
                mockMvc.perform(get("/places/{" + placeId + "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                );
        findActions
                .andDo(print())
                .andExpect(status().isOk())      // HttpStatus.OK(200)
                .andExpect(content().contentType("application/json;charset=utf-8"))     // contentType 검증
                .andExpect(jsonPath("$.meta.code").value(200))
//                .andExpect(jsonPath("$.data").value())
        ;
    }


    @Test
    @DisplayName("리뷰 수정 확인")
    void updateReview() throws Exception {
        Long reviewId = 1L;
        List<String> updatedImgUrl = new ArrayList<String>(Arrays.asList("http://s3-img-url-test1.com"));
        Integer stars = 3;

        ReviewUpdateRequestDto requestDto = new ReviewUpdateRequestDto("수정된 리뷰 테스트", updatedImgUrl, stars);
        final ResultActions actions =
                mockMvc.perform(put("/reviews/{" + reviewId + "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(new ObjectMapper().writeValueAsString(requestDto))
                );
        actions
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
        final ResultActions actions =
                mockMvc.perform(delete("/reviews/{" + reviewId + "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                );
        actions
                .andDo(print())
                .andExpect(status().isOk())      // HttpStatus.OK(200)
                .andExpect(content().contentType("application/json;charset=utf-8"))     // contentType 검증
                .andExpect(jsonPath("$.meta.code").value(200))
                .andExpect(jsonPath("$.data").isEmpty())
        ;
    }
}