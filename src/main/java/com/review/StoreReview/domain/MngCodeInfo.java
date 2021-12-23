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
    private Integer SEA;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "CODE_ID")
    private MngCode CODE_ID;

    // 1. 테이블과 매핑될 컬럼 = "CODE_NAME"/
    @Column(name = "CODE_NAME", nullable = false, length=100)
    private String CODE_NAME;

    // 1. 테이블과 매핑될 컬럼 = "DESC"/
    @Column(name = "DESC", nullable = false, length=100, columnDefinition = "VARCHAR(100) Default ''")
    private String DESC;

    @Column(name = "SORT_ORDER", nullable = false, columnDefinition = "INTEGER Default 0")
    private Integer SORT_ORDER;

    // 1. 테이블과 매핑될 컬럼 = "USE_YN"/
    @Column(name = "USE_YN", nullable = false, columnDefinition = "CHAR(1) Default 'N'")
    private char USE_YN;

    @Builder
    public MngCodeInfo(MngCode CODE_ID, String CODE_NAME, String DESC, Integer SORT_ORDER, char USE_YN) {
        this.CODE_ID = CODE_ID;
        this.CODE_NAME = CODE_NAME;
        this.DESC = DESC;
        this.SORT_ORDER = SORT_ORDER;
        this.USE_YN = USE_YN;
    }

}
