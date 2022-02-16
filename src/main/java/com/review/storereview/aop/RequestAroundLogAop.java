package com.review.storereview.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.review.storereview.common.enumerate.ApiStatusCode;
import com.review.storereview.common.exception.ReviewServiceException;
import com.review.storereview.dao.JWTUserDetails;
import com.review.storereview.dao.cms.ApiLog;
import com.review.storereview.dto.ResponseJsonObject;
import com.review.storereview.dto.request.ServiceRequestDTO;
import com.review.storereview.service.cms.LogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;

/** Class       : RqeustAroundLogAop (AOP)
 *  Author      : 조 준 희
 *  Description : com.review.storereview.controller.* 패키지 내 *Contorller.class 모든 클래스의
 *  메소드 모두 가로챔 => Request 정보 및 처리 결과를 LogService를 통해 Database에 저장
 *  History     : [2022-01-03] - 조 준희 - ApiLog Service, Repository 연동 후 정상적으로 Log Insert 개발 완료 ** 추후 Exception Return에 대해서 테스트 및 개발 필요.
 */

@Aspect
@Component
public class RequestAroundLogAop {
    private final ObjectMapper om ;
    private final LogService logService;

    @Autowired
    public RequestAroundLogAop(LogService logService) {
        this.om = new ObjectMapper().registerModule(new JavaTimeModule())
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
        this.logService = logService;
    }

    //  execution(* com.review.storereview.controller 하위 패키지 내에
    //   *Controller 클래스의 모든 메서드 Around => Pointcut 설정
    @Around(value = "execution(* com.review.storereview.controller..*Controller.*(..))")
    public Object ApiLog(ProceedingJoinPoint joinPoint) throws Throwable { // 파라미터 : 프록시 대상 객체의 메서드를 호출할 때 사용
        Object[] arguments   = joinPoint.getArgs();

        for(int i = 0 ; i < arguments.length ; i++)
        {
            if(arguments[i] instanceof MultipartFile == true)
                arguments[i] = String.format("MultiPartFile : %s",((MultipartFile) arguments[i]).getName() );
        }
        String  inputParam = om.writeValueAsString(arguments) ;
        String  outputMessage = "" ;
        char apiStatus = 'Y';
        String methodName = "";
        // Api log 담는 객체에 필요한 요소
        // ID(identity) / DATE(datetime) / SUID(varchar_12) / SAID(varchar_12) / API_NAME(varchar_20) / API_STATUS(char_1) / API_DESC(varchar_100) / PROCESS_TIME (Double)
        LocalDateTime date = LocalDateTime.now();
        String suid = "";
        String said = "";
        long elapsedTime = 0L;
        StringBuilder apiResultDescription = new StringBuilder();
        // joinPoint 리턴 객체 담을 변수
        Object retValue = null;
        StopWatch stopWatch = new StopWatch();

        try {
            // API 요청 사용자 정보 가져오기.
            Authentication authenticationToken = SecurityContextHolder.getContext().getAuthentication();
            if(authenticationToken.getPrincipal().equals("anonymousUser") == false){
                //인증 객체에 저장되어있는 유저정보 가져오기.
                JWTUserDetails userDetails = (JWTUserDetails) authenticationToken.getPrincipal();
                suid = userDetails.getSuid();
                said = userDetails.getSaid();
            }

            methodName  = joinPoint.getSignature().getName();   // 메소드 이름 => Api명

            // 서비스 처리 시간 기록 시작
            stopWatch.start();
            retValue = joinPoint.proceed();   // 실제 대상 객체의 메서드 호출

            outputMessage = om.writeValueAsString( ((ResponseEntity)retValue).getBody());

        }
        catch(ReviewServiceException ex)
        {
            apiStatus = 'N';
            outputMessage = om.writeValueAsString(ex.getResponseJsonObject());
            throw ex;
        }
        catch(Exception ex) {
            apiStatus='N';
            //Exception
            ResponseJsonObject resDto = ResponseJsonObject.withError(ApiStatusCode.SYSTEM_ERROR.getCode(),
                                                                ApiStatusCode.SYSTEM_ERROR.getType(),
                                                                ApiStatusCode.SYSTEM_ERROR.getMessage());
            outputMessage = om.writeValueAsString(resDto);
            throw ex;
        }
        finally {
            // 서비스 처리 시간 기록 종료
            stopWatch.stop();

            //api 처리 정보 => INPUT + OUTPUT   ** Exception이 떨어졌을때 Exception정보도 담는지 확인 필요 함.
            apiResultDescription.append("[INPUT]").append(System.lineSeparator())
                    .append(inputParam).append(System.lineSeparator())
                    .append("[OUTPUT]").append(System.lineSeparator())
                    .append(outputMessage).append(System.lineSeparator());

            elapsedTime = stopWatch.getTotalTimeMillis();
            String apiDesc = "";
            if(apiResultDescription.length() > 8000)
                apiDesc = apiResultDescription.toString().substring(0,8000);
            else
                apiDesc = apiResultDescription.toString();

            //API_LOG담을 객체 생성 ( "SUID", "SAID", 2022-01-14T12:55:22, "save", 'Y', [INPUT] [메서드 input] [OUTPUT] [메서드 output] , 3.0
            ApiLog data = new ApiLog(suid, said, date, methodName, apiStatus, apiDesc,elapsedTime*0.001);

            // INSERT
            logService.InsertApiLog(data);
        }

        return retValue;
    }

}
