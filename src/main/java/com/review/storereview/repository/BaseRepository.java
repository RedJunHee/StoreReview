package com.review.storereview.repository;

import com.review.storereview.dao.cust.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseRepository extends JpaRepository<User, String> {
    User findBySuid(String suid);

}
