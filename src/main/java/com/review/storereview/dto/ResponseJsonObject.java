package com.review.storereview.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.review.storereview.common.enumerate.ApiStatusCode;

import java.util.Map;

/** Class       : CommonResDto (Model)
 *  Author      : 조 준 희
 *  Description : 모든 API return 클래스   == 응답 객체
 *  History     : [2021-12-21] - TestCode : com.review.StoreReview.web.rest.controller.dto.CommonResDtoTest.class
 [2021-01-21] - Update : ExceptionResponseDto 클래스와 통합
 */

//Jackson어노테이션 json에 없는 프로퍼티 설정시 에러 무시 true
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseJsonObject {
    // meta
    private Meta meta = null;

    // 생성자
    public ResponseJsonObject(Meta meta) {
        this.meta = meta;
    }

    public Meta getMeta() {return meta;}

    // data
    private Object data;
    public Object getData() {return data;}      // data = null일 때는 필드에 나타나지 않음
    public ResponseJsonObject setData(Object val) {this.data = val; return this;}

    // 생성자
    public ResponseJsonObject(ApiStatusCode metaStatusCode) {
        this.meta = new Meta(metaStatusCode);
    }

    /**
     * 성공일 때 호출될 정적 팩터리 메서드
     * @param statusCode
     * @return ResponseJsonObject
     */
    public static ResponseJsonObject withStatusCode(ApiStatusCode statusCode) {
        Meta meta = new Meta(statusCode);
        return new ResponseJsonObject(meta);
    }

    /**
     * 실패일 때 호출될 meta 정적 팩터리 메서드
     * @param statusCode
     * @param errorType
     * @param errorMsg
     * @return
     */
    public static ResponseJsonObject withError(ApiStatusCode statusCode, String errorType,  String errorMsg) {
        Meta meta = new Meta(statusCode);
        meta.errorType = errorType;
        meta.errorMsg = errorMsg;
        return new ResponseJsonObject(meta);
    }

    /**
     * 파라미터 에러 시 호출될 meta 정적 팩터리 메서드
     * @param statusCode
     * @param errorType
     * @param errorMsg
     * @param parameterErrorMsg
     * @return
     */
    public static ResponseJsonObject withParameterMsg(ApiStatusCode statusCode, String errorType,  String errorMsg, Map parameterErrorMsg) {
        Meta meta = new Meta(statusCode);
        meta.errorType = errorType;
        meta.errorMsg = errorMsg;
        meta.parameterErrorMsg = parameterErrorMsg;
        return new ResponseJsonObject(meta);
    }

    //meta Class
    public static class Meta {
        // statusCode (not null)
        private ApiStatusCode statusCode = ApiStatusCode.NONE;
        public Integer getStatusCode(){ return statusCode.getCode(); }

        // errorType (null)  ex) "ParameterCheckFailed"
        private String errorType =null;
        public String getErrorType() { return statusCode.getType(); }

        // error Message  (null)  ex) "
        private String errorMsg =null;
        public String getErrorMsg() {
            return statusCode.getMessage();
        }

        // parameter Error Msg (null)
        // { "id": "공백일 수 없습니다",
        //    "password": "크기가 5와 20 사이여야 합니다"
        // }
        private Map parameterErrorMsg = null;
        public String getParameterErrorMsg() {
            //
            return "1";
        }

        /**
         * meta 생성자
         * @param statusCode
         */
        public Meta(ApiStatusCode statusCode) {
            this.statusCode = statusCode;
        }

    }
}