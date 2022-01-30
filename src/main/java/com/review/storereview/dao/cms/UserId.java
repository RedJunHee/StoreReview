package com.review.storereview.dao.cms;

import lombok.*;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Objects;

/**
 * Class       : UserId
 * Author      : 조 준 희
 * Description : User 엔티티 의 복합 키(suid, said)를 매핑하기 위한 클래스
 * History     : [2022-01-24] - 조 준희 - Class Create
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserId implements Serializable {
    @Column(name="SUID", nullable = false)
    private String suid;
    @Column(name="SAID", nullable = false)
    private String said;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId = (UserId) o;
        return Objects.equals(suid, userId.suid) && Objects.equals(said, userId.said);
    }

    @Override
    public int hashCode() {
        return Objects.hash(suid, said);
    }
}
