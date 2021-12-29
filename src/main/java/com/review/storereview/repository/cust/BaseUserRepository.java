package com.review.storereview.repository.cust;

import com.review.storereview.dao.cust.User;

import java.util.Optional;

public interface BaseUserRepository {
    Optional<User> findByIdAndPassword(String id,String password);
}
