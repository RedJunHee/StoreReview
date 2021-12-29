package com.review.storereview.repository.cust;

import com.review.storereview.dao.cust.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String>, BaseUserRepository {   // SUID를 id로 지정.
    Optional<User> findByIdAndPassword(String id, String password);
}
