package com.review.StoreReview.common;

import com.review.StoreReview.common.impl.KeyValueEnumImpl;


/** Class       : ApiStatusCode (Enum)
 *  Author      : 조 준 희
 *  Description : 모든 API에서 사용될 API 처리 상태 코드관리 Enum Class
 *                  - COMMON CODE - 공용 도메인 코드로 모든 도메인에서 사용될 공통 코드 분류
 *                  - ** 추가적인 도메인 코드가 생길 경우 //도메인명 CODE 와 같이 주석 후 코드 정리할 것.
 *  History     : [2021-12-21] - TEMP
 */

public enum ApiStatusCode implements KeyValueEnumImpl {

    //COMMON CODE
    OK (200,"OK","성공.")
    ,PARAMETER_CHECK_FAILED (400,"ParameterCheckFailed","문법상 또는 파라미터 오류가 있어서 서버가 요청사항을 처리하지 못함.")
    ,SYSTEM_ERROR(599,"SystemError", "시스템오류.")

    ;

    //Enum 필드
    private int code;
    private String name;
    private String desc;

    //Enum 생성자
    ApiStatusCode(int code, String name , String desc) {
        this.code = code;
        this.name = name;
        this.desc = desc;
    }

    @Override
    public Integer getKey() {
        return this.code;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }
}
