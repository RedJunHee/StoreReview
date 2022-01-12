package com.review.storereview.repository.cust;

import com.review.storereview.dao.cust.User;
import com.review.storereview.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface BaseUserRepository extends BaseRepository, JpaRepository<User, String> {
    Optional<User> findOneById(String id);
    Optional<User> findByIdAndPassword(String id, String password);
    boolean existsBySuid(String Suid);
}
