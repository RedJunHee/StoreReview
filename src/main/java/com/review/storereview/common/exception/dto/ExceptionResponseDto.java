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

    public ExceptionResponseDto(ApiStatusCode metaCode) {
        this.meta = new ExceptionResponseDto.Meta(metaCode);
    }
    public ExceptionResponseDto(ApiStatusCode metaCode, String extraErrorMessage) {
        this.meta = new ExceptionResponseDto.Meta(metaCode);
        meta.addExtraErrorMessage(extraErrorMessage);
    }

    //meta Class
    public static class Meta {
        // code
        private ApiStatusCode code = ApiStatusCode.NONE;
        public Integer getCode(){ return code.getCode(); }
        //msg
        private String error_type =null;
        public String getType() { return code.getType(); }
        //msg
        private String error_message =null;
        public String getMessage() {
            if(extraErrorMessage == null)
                return code.getMessage();
            else
                return code.getMessage() + "("+extraErrorMessage+")";
        }

        private String extraErrorMessage = null;
        public void addExtraErrorMessage (String str)
        {
            if(extraErrorMessage == null)
                this.extraErrorMessage = str;
            else
                this.extraErrorMessage += ", " + str;
        }

        public Meta(ApiStatusCode code) {
            this.code = code;
        }
    }
}
