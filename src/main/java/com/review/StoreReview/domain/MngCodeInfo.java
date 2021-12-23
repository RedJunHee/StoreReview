package com.review.StoreReview.domain;

import lombok.Builder;

import javax.persistence.*;


// 객체   : MngCodeInfo (Model)
// 설명   : MNG_DB.MngCodeInfo 테이블에 매핑될 Model
// 이름   : 조 준 희
// 생성   : 2021-12-21
// 기록   : [2021-12-21] - TEMP History/


public class MngCodeInfo {

    // 1. id 컬럼
    // 2. 테이블과 매핑될 컬럼 = "CODE_ID"
    // 3. GenerationType.IDENTITY DBMS에게 키 생성 권한을 맡김. /
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "SEQ")
    private Integer seq;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "CODE_ID")
    private MngCode codeId;

    // 1. 테이블과 매핑될 컬럼 = "CODE_NAME"/
    @Column(name = "CODE_NAME", nullable = false, length=100)
    private String codeName;

    // 1. 테이블과 매핑될 컬럼 = "DESC"/
    @Column(name = "DESC", nullable = false, length=100, columnDefinition = "VARCHAR(100) Default ''")
    private String desc;

    @Column(name = "SORT_ORDER", nullable = false, columnDefinition = "INTEGER Default 0")
    private Integer sortOrder;

    // 1. 테이블과 매핑될 컬럼 = "USE_YN"/
    @Column(name = "USE_YN", nullable = false, columnDefinition = "CHAR(1) Default 'N'")
    private char useYN;

    @Builder
    public MngCodeInfo(MngCode CODE_ID, String CODE_NAME, String DESC, Integer SORT_ORDER, char USE_YN) {
        this.codeId = CODE_ID;
        this.codeName = CODE_NAME;
        this.desc = DESC;
        this.sortOrder = SORT_ORDER;
        this.useYN = USE_YN;
    }

}
