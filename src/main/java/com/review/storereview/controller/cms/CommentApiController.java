package com.review.storereview.controller.cms;

import com.review.storereview.common.enumerate.ApiStatusCode;
import com.review.storereview.dao.cms.Comment;
import com.review.storereview.dto.ResponseJsonObject;
import com.review.storereview.dto.response.CommentResponseDto;
import com.review.storereview.dto.response.ReviewResponseDto;
import com.review.storereview.service.cms.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Class       : CommentApiController
 * Author      : 조 준 희
 * Description : Class Description
 * History     : [2022-01-24] - 조 준희 - Class Create
 */

@RestController
public class CommentApiController {
    private final CommentService commentService;

    @Autowired
    public CommentApiController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/comment/{reviewId}/{pageNo}")
    public ResponseEntity<ResponseJsonObject> findAllComments(@PathVariable("reviewId") Long reviewId, @PathVariable("pageNo") int pageNo)
    {

        // 페이지 요청 객체 생성
        PageRequest pageRequest = PageRequest.of(pageNo, 5, Sort.by("createdAt").descending());

        // 리뷰에 달린 코멘트 정보 모두 조회

        Page<Comment> comments = commentService.findAllComments(reviewId, pageRequest);

        // 응답 객체 초기화
        CommentResponseDto commentResponseDto = new CommentResponseDto(
            comments.getTotalElements()
        );

        for (Comment comment :comments.getContent()) {
            commentResponseDto.addComment(
                    new CommentResponseDto.comment(
                            comment.getId()
                            ,comment.getUser().getSuid()
                            ,comment.getUser().getSaid()
                            ,comment.getContent()
                            ,comment.getUser().getUserId()
                            ,comment.getCreatedAt()
                            ,comment.getUpdatedAt()
                    )
            );
        }

        // 결과값 리턴
        ResponseJsonObject resDto =  ResponseJsonObject.withStatusCode(ApiStatusCode.OK).setData(commentResponseDto);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }
}
