package com.review.storereview.repository.impl.cust;

import com.review.storereview.dao.cust.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {   // SUID를 id로 지정.
}
