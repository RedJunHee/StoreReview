package com.review.StoreReview.domain.MNG;

import com.review.StoreReview.domain.enums.UseYN;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


/** Class       : MngCode (Model)
 *  Author      : 조 준 희
 *  Description : MNG_DB.MngCode 테이블에 매핑될 Model
 *  History     : [2021-12-21] - TEMP History/
 */

@Entity
@Getter
@Table(name = "MNG_CODE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MngCode {

    // 1. id 컬럼
    // 2. 테이블과 매핑될 컬럼 = "CODE_ID"/
    @Id
    @Column(name= "CODE_ID")
    private String codeId;

    // 1. 테이블과 매핑될 컬럼 = "DESC"/
    @Column(name = "DESC", nullable = false, length=100, columnDefinition = "VARCHAR(100) Default ''")
    private String desc;

    // 1. 테이블과 매핑될 컬럼 = "USE_YN"/
    @Column(name = "USE_YN", nullable = false, columnDefinition = "dDefault 'N'")
    @Enumerated(EnumType.STRING)
    private UseYN useYN = UseYN.N; // 처음에는 모두 N으로 생성!

    @Builder
    public MngCode(String code_ID, String desc, UseYN use_YN)
    {
        this.codeId = code_ID;
        this.desc = desc;
        this.useYN = use_YN;
    }
}
