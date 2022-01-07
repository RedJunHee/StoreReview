package com.review.storereview.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.review.storereview.dao.log.Api_Log;
import com.review.storereview.service.log.LogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

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
        this.om = new ObjectMapper().registerModule(new JavaTimeModule());
        this.logService = logService;
    }

    //  execution(* com.review.storereview.controller 하위 패키지 내에
    //   *Controller 클래스의 모든 메서드 Around
    @Around(value = "execution(* com.review.storereview.controller.*.*Controller.*(..))")
    public Object ApiLog(ProceedingJoinPoint joinPoint) throws Throwable {

        final long time_stamp = System.currentTimeMillis();

        Object[] arguments   = joinPoint.getArgs();
        String  inputParam = Arrays.toString(arguments);
        String  outputMessage = "" ;

        // Api log 담는 객체에 필요한 요소
        // ID(identity) / DATE(datetime) / SUID(varchar_12) / SAID(varchar_12) / API_NAME(varchar_20) / API_STATUS(char_1) / API_DESC(varchar_100) / PROCESS_TIME (Double)
        LocalDateTime date = LocalDateTime.now();
        String suid = "TEST00000001";
        String said = "TEST00000002";
        String methodName  = joinPoint.getSignature().getName();   // 메소드 이름 => Api명
        final char status = 'Y'; // 성공 케이스
        StringBuilder apiResultDescription = new StringBuilder();
        long elapsed_time = 0L;

        StopWatch stopWatch = new StopWatch();
        // joinPoint 리턴 객체 담을 변수
        Object retValue = null;
        //
        try {
            // 서비스 처리 시간 기록 시작
            stopWatch.start();
            retValue = joinPoint.proceed();

            outputMessage = om.writeValueAsString( ((ResponseEntity)retValue).getBody());

            //api 처리 정보 => INPUT + OUTPUT   ** Exception이 떨어졌을때 Exception정보도 담는지 확인 필요 함.
            apiResultDescription.append("[INPUT]").append(System.lineSeparator())
                                .append(inputParam).append(System.lineSeparator())
                                .append("[OUTPUT]").append(System.lineSeparator())
                                .append(outputMessage).append(System.lineSeparator());

        } catch(Exception ex) {
            //Exception
            ex.printStackTrace();

        } finally {
            // 서비스 처리 시간 기록 종료
            stopWatch.stop();

            elapsed_time = stopWatch.getTotalTimeMillis();

            //API_LOG담을 객체 생성
            Api_Log data = new Api_Log(suid,said,date,methodName,status,apiResultDescription.toString(),elapsed_time*0.001);

            // INSERT
            logService.InsertApiLog(data);

        }

        return retValue;
    }

}
