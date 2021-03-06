package com.review.storereview.repository.cms;

import com.review.storereview.dao.cms.User;
import com.review.storereview.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface BaseUserRepository extends BaseRepository, JpaRepository<User, String> {
    Optional<User> findOneByUserId(String userId);
    Optional<User> findByUserIdAndPassword(String userId, String password);
    boolean existsByUserId(String userId);
}
