package com.review.storereview.dao.cust;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * Class       : UserId
 * Author      : 조 준 희
 * Description : Class Description
 * History     : [2022-01-24] - 조 준희 - Class Create
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserId implements Serializable {
    private String suid;
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
