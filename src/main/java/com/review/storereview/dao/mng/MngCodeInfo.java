package com.review.storereview.dao.mng;

import com.review.storereview.common.enumerate.UseYN;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;



/** Class       : MngCodeInfo (Model)
 *  Author      : 조 준 희
 *  Description : MNG_DB.MngCodeInfo 테이블에 매핑될 Model
 *  History     : [2021-12-21] - TEMP History/
 */
@Entity
@Getter
@Table(name= "MNG_CODE_INFO")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MngCodeInfo {

    // 1. id 컬럼
    // 2. 테이블과 매핑될 컬럼 = "CODE_ID"
    // 3. GenerationType.IDENTITY DBMS에게 키 생성 권한을 맡김.
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
    @Column(name = "USE_YN", nullable = false, columnDefinition = "Default 'N'")
    @Enumerated(EnumType.STRING)
    private UseYN useYN = UseYN.N; // 처음에는 모두 N으로 생성!

    @Builder
    public MngCodeInfo(MngCode CODE_ID, String CODE_NAME, String DESC, Integer SORT_ORDER, UseYN USE_YN) {
        this.codeId = CODE_ID;
        this.codeName = CODE_NAME;
        this.desc = DESC;
        this.sortOrder = SORT_ORDER;
        this.useYN = USE_YN;
    }

}
