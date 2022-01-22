package com.review.storereview.dao;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@EntityListeners(AuditingEntityListener.class)      // Auditing 기능을 포함
@MappedSuperclass   //이 class를 상속받으면 필드를 column으로 인식
public abstract class BaseTimeEntity {
    @CreatedDate
    @Column(updatable = false, name="CREATED_AT")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name="UPDATED_AT")
    private LocalDateTime updatedAt;
}
