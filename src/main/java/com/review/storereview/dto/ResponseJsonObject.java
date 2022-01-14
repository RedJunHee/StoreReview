package com.review.storereview.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.review.storereview.common.enumerate.ApiStatusCode;
import com.review.storereview.common.exception.dto.ExceptionResponseDto;

/** Class       : CommonResDto (Model)
 *  Author      : 조 준 희
 *  Description : 모든 API return 클래스   == 응답 객체
 *  History     : [2021-12-21] - TestCode : com.review.StoreReview.web.rest.controller.dto.CommonResDtoTest.class
 */

//Jackson어노테이션 json에 없는 프로퍼티 설정시 에러 무시 true
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseJsonObject {
    private Meta meta = null;
    public Meta getMeta() {return meta;}

    // data
    private Object data;
    public Object getData() {return data;}      // data = null일 때는 필드에 나타나지 않음
    public ResponseJsonObject setData(Object val) {this.data = val; return this;}

    public ResponseJsonObject(ApiStatusCode metaStatusCode) {
        this.meta = new Meta(metaStatusCode);
    }

    //meta Class
    public static class Meta {
        // code
        private ApiStatusCode statusCode = ApiStatusCode.NONE;
        public Integer getCode(){ return statusCode.getCode(); }

        public Meta(ApiStatusCode statusCode) {
            this.statusCode = statusCode;
        }
    }
}