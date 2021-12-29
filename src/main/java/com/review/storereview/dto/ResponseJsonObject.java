package com.review.storereview.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.review.storereview.common.enumerate.ApiStatusCode;


/** Class       : CommonResDto (Model)
 *  Author      : 조 준 희
 *  Description : 모든 API return 클래스   == 응답 객체
 *  History     : [2021-12-21] - TestCode : com.review.StoreReview.web.rest.controller.dto.CommonResDtoTest.class
 */


//Jackson어노테이션 json에 없는 프로퍼티 설정시 에러 무시 true
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseJsonObject {
    //meta
    private ResponseJsonObject.Meta meta = null;
    public ResponseJsonObject.Meta getMeta() {return meta;}
    private void setMeta(ResponseJsonObject.Meta val) {this.meta = val;}

    private Object data = null;
    public Object getData() {return data;}
    private void setData(Object val) {this.data = val;}

    //meta Class
    public static class Meta {
        // code
        private ApiStatusCode code = ApiStatusCode.NONE;
        public ApiStatusCode getCode(){ return code; }
        public void setCode(ApiStatusCode code) { this.code = code; }
        //msg
        private String type =null;
        public String getType() { return code.getType(); }
        //msg
        private String message =null;
        public String getMessage() { return code.getMessage(); }

        //Meta Builder Pattern
        public final static class Builder{
            private ApiStatusCode code = ApiStatusCode.NONE;
            public Builder withCode(ApiStatusCode val) {this.code = val; return this;}
            public ResponseJsonObject.Meta build() {return new ResponseJsonObject.Meta(this);}
        }
        //Meta Builder Pattern 생성자
        public static Builder builder() {return new Builder();}
        public Meta(){}
        private Meta(Builder builder) {
            this.code = builder.code;
        }
    }

    //CommonResDto Builder Pattern
    public final static class Builder {
        private ResponseJsonObject.Meta meta = null;
        private Object data = null;

        public Builder withMeta(ResponseJsonObject.Meta val) { this.meta = val; return this; }
        public Builder withData(Object val) { this.data = val; return this; }

        public ResponseJsonObject build() { return new ResponseJsonObject(this); }
    }
    //CommonResDto Builder Pattern 생성자
    public static Builder builder() { return new Builder(); }
    public ResponseJsonObject() {}
    private ResponseJsonObject(Builder builder) {
        this.meta = builder.meta;
        this.data = builder.data;
    }
}