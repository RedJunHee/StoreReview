package com.review.storereview.service.cms;

import com.review.storereview.dao.cms.Comment;
import com.review.storereview.repository.cms.BaseCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Class       : CommentService
 * Author      : 조 준 희
 * Description : Class Description
 * History     : [2022-01-24] - 조 준희 - Class Create
 */

@Service
public class CommentService {

    private final BaseCommentRepository commentRepository;

    @Autowired
    public CommentService(BaseCommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Page<Comment> findAllComments(Long reviewId, PageRequest pageRequest) {
            Page<Comment> comments = commentRepository.findAllByReview(reviewId, pageRequest);
        return comments;
    }
}
