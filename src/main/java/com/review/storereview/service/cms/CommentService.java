package com.review.storereview.service.cms;

import com.review.storereview.dao.cms.Comment;
import com.review.storereview.repository.cms.BaseCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * Class       : CommentService
 * Author      : 조 준 희
 * Description : Class Description
 * History     : [2022-01-24] - 조 준희 - Class Create
 * History     : [2022-01-24] - 조 준희 - Class Create
 */

@Service
public class CommentService {

    private final BaseCommentRepository commentRepository;

    @Autowired
    public CommentService(BaseCommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Page<Comment> findAllCommentsAndIsDelete(Long reviewId, Integer IsDelete, PageRequest pageRequest) {
            Page<Comment> comments = commentRepository.findAllByReviewIdAndIsDelete(reviewId, IsDelete, pageRequest);
        return comments;
    }

    public Comment save(Comment comment)
    {
        Comment saveComment = commentRepository.save(comment);

        return saveComment;
    }
    public Comment findByCommentId(Long commentId){

        return commentRepository.findByCommentId(commentId);

    }
    // 리뷰 응답 시 코멘트 갯수 조회
    public int findCommentNumByReviewId(Long reviewId) {
        return commentRepository.findCommentNumByReviewId(reviewId);
    }
}
