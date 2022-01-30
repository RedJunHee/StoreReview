package com.review.storereview.controller.cms;

import com.review.storereview.common.enumerate.ApiStatusCode;
import com.review.storereview.common.exception.ContentNotFound;
import com.review.storereview.common.utils.CryptUtils;
import com.review.storereview.common.utils.StringUtil;
import com.review.storereview.dao.JWTUserDetails;
import com.review.storereview.dao.cms.Comment;
import com.review.storereview.dao.cms.User;
import com.review.storereview.dto.ResponseJsonObject;
import com.review.storereview.dto.request.CommentDeleteRequestDto;
import com.review.storereview.dto.request.CommentUpdateRequestDto;
import com.review.storereview.dto.request.CommentWriteRequestDto;
import com.review.storereview.dto.response.CommentDeleteResponseDto;
import com.review.storereview.dto.response.CommentListResponseDto;
import com.review.storereview.dto.response.CommentResponseDto;
import com.review.storereview.service.cms.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

/**
 * Class       : CommentApiController
 * Author      : 조 준 희
 * Description : Class Description
 * History     : [2022-01-24] - 조 준희 - Class Create
 */

@RestController
public class CommentApiController {
    private final Logger logger = LoggerFactory.getLogger(CommentApiController.class);

    private final CommentService commentService;

    @Autowired
    public CommentApiController(CommentService commentService) {
        this.commentService = commentService;
    }

    /** 특정 리뷰아이디에 달린 코멘트 리스트 조회
     * @param reviewId 리뷰 아이디
     * @param pageNo 페이지 번호
     * @return
     * @throws ContentNotFound
     */

    @GetMapping("/comment/{reviewId}/{pageNo}")
    public ResponseEntity<ResponseJsonObject> findAllComments(@PathVariable("reviewId") Long reviewId, @PathVariable("pageNo") int pageNo)throws ContentNotFound
    {
        try {
            // 페이지 요청 객체 생성
            PageRequest pageRequest = PageRequest.of(pageNo, 5, Sort.by("createdAt").ascending());

            // 리뷰에 달린 코멘트 정보 모두 조회
            Page<Comment> comments = commentService.findAllCommentsAndIsDelete(reviewId,0, pageRequest);
            if (comments.getNumberOfElements() == 0) {
                // 결과값 리턴
                // 콘텐츠 없음.... 204 Code
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                //throw new ContentNotFound();
            }

            // 응답 객체 초기화
            CommentListResponseDto commentResponseDto = new CommentListResponseDto(
                    comments.getTotalElements()
            );

            for (Comment comment : comments.getContent()) {
                commentResponseDto.addComment(
                        new CommentListResponseDto.comment(
                                comment.getCommentId()
                                , comment.getUser().getSaid()
                                , CryptUtils.Base64Encoding(comment.getContent())
                                , comment.getUser().getUserId()
                                , StringUtil.DateTimeToString(comment.getCreatedAt())
                                , StringUtil.DateTimeToString(comment.getUpdatedAt())
                        )
                );
            }

            // 결과값 리턴
            ResponseJsonObject resDto = ResponseJsonObject.withStatusCode(ApiStatusCode.OK).setData(commentResponseDto);
            return new ResponseEntity<>(resDto, HttpStatus.OK);
        }catch(Exception ex)
        {
            logger.error(ex.getMessage());
            ResponseJsonObject resDto = ResponseJsonObject.withError(ApiStatusCode.SYSTEM_ERROR,ApiStatusCode.SYSTEM_ERROR.getType(),ApiStatusCode.SYSTEM_ERROR.getMessage());
            return new ResponseEntity<ResponseJsonObject>(resDto,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     *  코멘트 작성 API
     *
     * @param requestDto
     * @return 작성된 코멘트 정보 리턴.
     */
    @PostMapping("/comment")
    public ResponseEntity<ResponseJsonObject> save(@RequestBody CommentWriteRequestDto requestDto)
    {
        try {
            // 코멘트 내용 디코딩 처리.
            String decodingContent = CryptUtils.Base64Decoding(requestDto.getContent());

            //인증된 사용자의 인증객체 가져오기
            Authentication authenticationToken = SecurityContextHolder.getContext().getAuthentication();
            //인증 객체에 저장되어있는 유저정보 가져오기.
            JWTUserDetails userDetails = (JWTUserDetails) authenticationToken.getPrincipal();

            //저장하고자 하는 코멘트 객체 생성.
            Comment comment = Comment.builder().reviewId(requestDto.getReviewId())
                    .content(decodingContent)
                    .user(User.builder()
                            .userId(userDetails.getUsername())
                            .suid(userDetails.getSuid())
                            .said(userDetails.getSaid())
                            .build())
                    .createdAt(LocalDateTime.now())
                    .IsDelete(0)
                    .build();


            // 코멘트 작성 서비스 처리
            Comment saveComment = commentService.save(comment);

            // ResponseDto 생성
            CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                    .commentId(saveComment.getCommentId())
                    .userId(saveComment.getUser().getUserId())
                    .said(saveComment.getUser().getSaid())
                    .content(CryptUtils.Base64Encoding(saveComment.getContent()))
                    .createdAt(StringUtil.DateTimeToString(saveComment.getCreatedAt()))
                    .updatedAt(StringUtil.DateTimeToString(saveComment.getUpdatedAt()))
                    .build();

            ResponseJsonObject resDto = ResponseJsonObject.withStatusCode(ApiStatusCode.CREATED).setData(commentResponseDto);
            return new ResponseEntity<>(resDto, HttpStatus.CREATED);
        }
        catch(Exception ex)
        {
            logger.error(ex.getMessage());
            ResponseJsonObject resDto = ResponseJsonObject.withError(ApiStatusCode.SYSTEM_ERROR,ApiStatusCode.SYSTEM_ERROR.getType(),ApiStatusCode.SYSTEM_ERROR.getMessage() );
            return new ResponseEntity<ResponseJsonObject>(resDto,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/comment")
    public ResponseEntity<ResponseJsonObject> writeComment(@RequestBody CommentUpdateRequestDto requestDto)
    {
        try {
            // content Base64 디코딩 작업
            String decodingContent = CryptUtils.Base64Decoding(requestDto.getContent());

            // Request 사용자 인증 객체 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // 사용자 인증 객체에서 사용자 정보 가져오기
            JWTUserDetails userDetails = (JWTUserDetails) authentication.getPrincipal();

            // 코멘트 수정 서비스
            Comment savedComment = commentService.findByCommentId(requestDto.getCommentId());

            Comment comment = Comment.builder()
                    .commentId(requestDto.getCommentId())
                    .user(savedComment.getUser())
                    .content(decodingContent)
                    .updatedAt(LocalDateTime.now())
                    .createdAt(savedComment.getCreatedAt())
                    .reviewId(savedComment.getReviewId())
                    .IsDelete(0)
                    .build();

            Comment updateComment = commentService.save(comment);

            CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                    .commentId(updateComment.getCommentId())
                    .userId(updateComment.getUser().getUserId())
                    .said(updateComment.getUser().getSaid())
                    .content(CryptUtils.Base64Encoding(updateComment.getContent()))
                    .createdAt(StringUtil.DateTimeToString(updateComment.getCreatedAt()))
                    .updatedAt(StringUtil.DateTimeToString(updateComment.getUpdatedAt()))
                    .build();

            // ResponseDto 작성
            ResponseJsonObject resDto = ResponseJsonObject.withStatusCode(ApiStatusCode.OK).setData(commentResponseDto);
            return new ResponseEntity<ResponseJsonObject>(resDto,HttpStatus.OK);

        }
        catch(Exception ex)
        {
            logger.error(ex.getMessage());
            ResponseJsonObject resDto = ResponseJsonObject.withError(ApiStatusCode.SYSTEM_ERROR,ApiStatusCode.SYSTEM_ERROR.getType(),ApiStatusCode.SYSTEM_ERROR.getMessage());
            return new ResponseEntity<ResponseJsonObject>(resDto,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/comment")
    public ResponseEntity<ResponseJsonObject> deleteComment(@RequestBody CommentDeleteRequestDto requestDto)
    {
        try {
            // 서비스 요청 사용자 인증 객체 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // 사용자 정보 가져오기.
            JWTUserDetails userDetails = (JWTUserDetails) authentication.getPrincipal();

            // 코멘트 작성자 유효성 체크
            Comment comment = commentService.findByCommentId(requestDto.getCommentId());

            if (comment.getUser().getSuid().equals(userDetails.getSuid())) {
                comment.setisDelete(1);
                // 코멘트 삭제 서비스 처리
                Comment deleteComment = commentService.save(comment);

                // ResponseDto 작성
                CommentDeleteResponseDto responseDto = CommentDeleteResponseDto.builder()
                        .commentId(deleteComment.getCommentId())
                        .build();

                // ResponseDto 작성
                ResponseJsonObject resDto = ResponseJsonObject.withStatusCode(ApiStatusCode.OK).setData(responseDto);
                return new ResponseEntity<ResponseJsonObject>(resDto, HttpStatus.OK);
            } else {
                logger.info("Comment delete failed!!");
                return new ResponseEntity<>(ResponseJsonObject.withStatusCode(ApiStatusCode.FORBIDDEN), HttpStatus.FORBIDDEN);
            }
        }catch(Exception ex)
        {
            logger.error(ex.getMessage());
            ResponseJsonObject resDto = ResponseJsonObject.withError(ApiStatusCode.SYSTEM_ERROR,ApiStatusCode.SYSTEM_ERROR.getType(),ApiStatusCode.SYSTEM_ERROR.getMessage());
            return new ResponseEntity<ResponseJsonObject>(resDto,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
