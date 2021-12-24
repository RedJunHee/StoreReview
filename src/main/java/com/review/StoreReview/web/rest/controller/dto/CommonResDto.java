package com.review.StoreReview.web.rest.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/** Class       : CommonResDto (Model)
 *  Author      : 조 준 희
 *  Description : 모든 API return 클래스   == 응답 객체
 *  History     : [2021-12-21] - TestCode : com.review.StoreReview.web.rest.controller.dto.CommonResDtoTest.class
 */

//Jackson어노테이션 json에 없는 프로퍼티 설정시 에러 무시 true
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonResDto {
    private static String NL = System.lineSeparator();

    //meta

    private CommonResDto.Meta meta = null;
    private CommonResDto.Meta getMeta() {return meta;}
    public void setMeta(CommonResDto.Meta val) {this.meta = val;}

    //meta Class
    public static class Meta {
        // code
        private long code = 0;
        public long getCode(){ return code; }
        public void setCode(long code) { this.code = code; }

        //msg
        private String msg =null;
        public String getMsg() { return msg; }
        public void setMsg(String msg) { this.msg = msg; }

        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("code" + "=" + getCode());
            sb.append(",");
            sb.append("msg" + "=" + getMsg());
            return sb.toString();
        }

        //Meta Builder Pattern
        public final static class Builder{
            private long code = 0;
            public Builder withCode(long val) {this.code = val; return this;}
            private String msg = null;
            public Builder withMsg(String val) {this.msg = val; return this;}
            public CommonResDto.Meta build() {return new CommonResDto.Meta(this);}
        }
        //Meta Builder Pattern 생성자
        public static Builder builder() {return new Builder();}
        public Meta(){}
        private Meta(Builder builder) {
            this.code = builder.code;
            this.msg = builder.msg;
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (meta != null) sb.append("meta" + "={" + meta.toString() + "}");
        else sb.append("meta" + "=null");
        return sb.toString();
    }

    //CommonResDto Builder Pattern
    public final static class Builder {
        private CommonResDto.Meta meta = null;
        public Builder withMeta(CommonResDto.Meta val) { this.meta = val; return this; }

        public CommonResDto build() { return new CommonResDto(this); }
    }
    //CommonResDto Builder Pattern 생성자
    public static Builder builder() { return new Builder(); }
    public CommonResDto() {}
    private CommonResDto(Builder builder) {
        this.meta = builder.meta;
    }
}
