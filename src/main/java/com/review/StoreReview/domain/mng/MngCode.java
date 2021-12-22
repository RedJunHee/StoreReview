package com.review.StoreReview.domain.mng;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


// 객체   : MngCode (Model)
// 설명   : MNG_DB.MngCode 테이블에 매핑될 Model
// 이름   : 조 준 희
// 생성   : 2021-12-21
// 기록   : [2021-12-21] - TEMP History/


@Entity
@Getter
@Table(name = "MNG_CODE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MngCode {

    // 1. id 컬럼
    // 2. 테이블과 매핑될 컬럼 = "CODE_ID"/
    @Id
    @Column(name= "CODE_ID")
    private String CODE_ID;

    // 1. 테이블과 매핑될 컬럼 = "DESC"/
    @Column(name = "DESC", nullable = false, length=100, columnDefinition = "VARCHAR(100) Default ''")
    private String DESC;

    // 1. 테이블과 매핑될 컬럼 = "USE_YN"/
    @Column(name = "USE_YN", nullable = false, columnDefinition = "CHAR(1) Default 'N'")
    private char USE_YN;

    @Builder
    public MngCode(String code_ID, String desc, char use_YN)
    {
        this.CODE_ID = code_ID;
        this.DESC = desc;
        this.USE_YN = use_YN;
    }
}
