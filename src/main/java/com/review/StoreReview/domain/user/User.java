package com.review.StoreReview.domain.user;

import javax.persistence.Column;
import javax.persistence.*;

@Entity
public class User {

    // 다른 객체에서 필요하여 먼저 선언했습니다.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long id;

}
