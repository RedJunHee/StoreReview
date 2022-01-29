package com.review.storereview.service.cms;

import com.review.storereview.dao.cms.Comment;
import com.review.storereview.dao.cms.User;
import com.review.storereview.repository.cms.BaseCommentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class CommentServiceTest {

    @Autowired
    BaseCommentRepository commentRepository;

    @Test
    public void commentWrite()
    {
        Comment comment = Comment.builder()
                .reviewId(1L)
                .content("테스크 코드에서 작성한 코멘트 입니다.")
                .createdAt(LocalDateTime.now())
                .user(User.builder()
                        .userId("test@review.com")
                        .said("TE0000000001")
                        .suid("TE0000000001")
                        .build())
                .build();

        Comment saveComment = commentRepository.save(comment);

        Assertions.assertEquals(saveComment.getUser().getUserId(), "test@review.com");
        Assertions.assertEquals(saveComment.getContent(), "테스크 코드에서 작성한 코멘트 입니다.");

    }
}