package com.review.storereview.repository.cust;

import com.review.storereview.dao.cust.User;
import com.review.storereview.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BaseUserRepository extends BaseRepository {
    Optional<User> findByIdAndPassword(String id, String password);

}
