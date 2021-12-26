package com.review.StoreReview.repository;

import com.review.StoreReview.domain.CUST.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {   // SUID를 id로 지정.
    Optional<User> findBySUID(String suid);     // null값이 나올 경우 대비
}
