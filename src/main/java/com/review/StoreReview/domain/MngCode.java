package com.review.StoreReview.domain;

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
    private String dodeId;

    // 1. 테이블과 매핑될 컬럼 = "DESC"/
    @Column(name = "DESC", nullable = false, length=100, columnDefinition = "VARCHAR(100) Default ''")
    private String desc;

    // 1. 테이블과 매핑될 컬럼 = "USE_YN"/
    @Column(name = "USE_YN", nullable = false, columnDefinition = "CHAR(1) Default 'N'")
    private char useYN;

    @Builder
    public MngCode(String code_ID, String desc, char use_YN)
    {
        this.dodeId = code_ID;
        this.desc = desc;
        this.useYN = use_YN;
    }
}