package com.review.storereview.repository.cms;

import com.review.storereview.dao.cms.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class       : BaseCommentRepositoryTest
 * Author      : 조 준 희
 * Description : Class Description
 * History     : [2022-01-24] - 조 준희 - Class Create
 */

@SpringBootTest
class BaseCommentRepositoryTest {

    @Autowired
    private BaseCommentRepository commentRepository;


    @Test
    public void findAllComments()
    {
        Integer page = 1;
        Long reviewId = 1L;

        PageRequest pageRequest = PageRequest.of(page, 5);
//        Page<Comment> comments = commentRepository.findAllByReview(reviewId,pageRequest);

//        System.out.println("토탈 사이즈 : " + comments.getTotalElements());

    }

}