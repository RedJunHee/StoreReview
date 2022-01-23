package com.review.storereview.repository.cust;

import com.review.storereview.dao.cust.User;
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

    @Query(value="SELECT USER_ID FROM USER_INFO", nativeQuery = true)
    List<Object[]> findUserIdBySuid(String userId);     // userId 조인 시 필요
}
