package com.review.storereview.common.exception.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.review.storereview.common.enumerate.ApiStatusCode;
import lombok.AllArgsConstructor;

//Jackson어노테이션 json에 없는 프로퍼티 설정시 에러 무시 true
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExceptionResponseDto {
    //meta
    private ExceptionResponseDto.Meta meta = null;
    public ExceptionResponseDto.Meta getMeta() {return meta;}
    private void setMeta(ExceptionResponseDto.Meta val) {this.meta = val;}

    //meta Class
    public static class Meta {
        // code
        private ApiStatusCode code = ApiStatusCode.NONE;
        public Integer getCode(){ return code.getCode(); }
        public void setCode(ApiStatusCode code) { this.code = code; }
        //msg
        private String error_type =null;
        public String getType() { return code.getType(); }
        //msg
        private String error_message =null;
        public String getMessage() { return code.getMessage(); }

        //Meta Builder Pattern
        public final static class Builder{
            private ApiStatusCode code = ApiStatusCode.NONE;
            public Builder withCode(ApiStatusCode val) {this.code = val; return this;}
            public ExceptionResponseDto.Meta build() {return new ExceptionResponseDto.Meta(this);}
        }
        //Meta Builder Pattern 생성자
        public static ExceptionResponseDto.Meta.Builder builder() {return new ExceptionResponseDto.Meta.Builder();}
        public Meta(){}
        private Meta(ExceptionResponseDto.Meta.Builder builder) {
            this.code = builder.code;
        }
    }

    //CommonResDto Builder Pattern
    public final static class Builder {
        private ExceptionResponseDto.Meta meta = null;

        public Builder withMeta(ExceptionResponseDto.Meta val) { this.meta = val; return this; }
        public ExceptionResponseDto build() { return new ExceptionResponseDto(this); }
    }
    //ExceptionResponseDto Builder Pattern 생성자
    public static Builder builder() { return new ExceptionResponseDto.Builder(); }

    private ExceptionResponseDto(Builder builder) {
        this.meta = builder.meta;
    }
}
