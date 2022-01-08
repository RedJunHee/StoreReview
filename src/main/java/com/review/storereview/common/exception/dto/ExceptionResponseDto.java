package com.review.storereview.common.exception.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.review.storereview.common.enumerate.ApiStatusCode;
import lombok.AllArgsConstructor;

import java.util.Map;

//Jackson어노테이션 json에 없는 프로퍼티 설정시 에러 무시 true
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExceptionResponseDto {
    //meta
    private ExceptionResponseDto.Meta meta = null;
    public ExceptionResponseDto.Meta getMeta() {return meta;}

    /**
     * metaStatusCode 포함한 정적 팩토리 메소드
     * @param metaStatusCode
     * @return
     */
    public static ExceptionResponseDto createMetaDto(ApiStatusCode metaStatusCode){
        return new ExceptionResponseDto(metaStatusCode);
    }

    /**
     *  metaStatusCode, Message 포함한 정적 팩토리 메소드
     * @param metaStatusCode
     * @param extraErrorMessage
     * @return
     */
    public static ExceptionResponseDto createMetaMessageDto(ApiStatusCode metaStatusCode, String extraErrorMessage)
    {
        return new ExceptionResponseDto(metaStatusCode, extraErrorMessage);
    }

//    public static ExceptionResponseDto createMetaMapDto(ApiStatusCode metaStatusCode, Map<String, Object> extraErrorMessages)
//    {
//        return new ExceptionResponseDto(metaStatusCode, extraErrorMessages);
//    }
    private ExceptionResponseDto(ApiStatusCode metaStatusCode) {
        this.meta = new ExceptionResponseDto.Meta(metaStatusCode);
    }

    private ExceptionResponseDto(ApiStatusCode metaStatusCode, String extraErrorMessage) {
        this.meta = new ExceptionResponseDto.Meta(metaStatusCode);
        meta.addExtraErrorMessage(extraErrorMessage);
    }

//    private ExceptionResponseDto(ApiStatusCode metaStatusCode, Map<String, Object> errorMessageMap) {
//        this.meta = new ExceptionResponseDto.Meta(metaStatusCode);
//        meta.addExtraErrorMessageMap(errorMessageMap);
//    }
    //meta Class
    public static class Meta {
        // code
        private ApiStatusCode statusCode = ApiStatusCode.NONE;
        public Integer getStatusCode(){ return statusCode.getCode(); }
        //msg
        private String errorType =null;
        public String getErrorType() { return statusCode.getType(); }
        //msg
        private String errorMessage =null;
        public String getErrorMessage() {
            if(extraErrorMessage == null)
                return statusCode.getMessage();
            else
                return statusCode.getMessage() + "("+extraErrorMessage+")";
        }

        private String extraErrorMessage = null;
        public void addExtraErrorMessage (String str)
        {
            if(extraErrorMessage == null)
                this.extraErrorMessage = str;
            else
                this.extraErrorMessage += ", " + str;
        }

//        public void addExtraErrorMessages (Map<String, Object> errorMap)
//        {
//        }

        public Meta(ApiStatusCode statusCode) {
            this.statusCode = statusCode;
        }
    }
}
