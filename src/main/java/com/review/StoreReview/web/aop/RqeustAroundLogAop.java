package com.review.StoreReview.web.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import java.util.Arrays;

/** Class       : RqeustAroundLogAop (AOP)
 *  Author      : 조 준 희
 *  Description : com.review.StoreRevie.web.rest.controller 패키지 내 *Contorller.class 모든 클래스의
 *  메소드 모두 가로챔 => Request 정보 및 처리 결과를 LogService를 통해 Database에 저장
 *  History     : [2021-12-21] - TEMP
 */

@Aspect
@Component
public class RqeustAroundLogAop {

    //  execution(* com.review.StoreRevie.web.rest.controller 패키지 내에
    //   *Controller 클래스의 모든 메서드 Around
    @Around(value = "execution(* com.review.StoreReview.web.rest.controller.*Controller.*(..))")
    public Object ApiLog(ProceedingJoinPoint joinPoint) throws Throwable {

        final long time_stamp = System.currentTimeMillis();

        String   className   = joinPoint.getSignature().getDeclaringType().getName();
        String   serviceName = className.substring(className.lastIndexOf(".") + 1, className.length());
        String   methodName  = joinPoint.getSignature().getName();
        Object[] arguments   = joinPoint.getArgs();

        String serviceMethodName = serviceName + "." + methodName + "()";
        //
        System.out.println( Long.toString( time_stamp ) + " >> " + "[In       ] " + serviceMethodName + " : Method Process Start");
        System.out.println( Long.toString( time_stamp ) + " >> " + "[Arguments] " + Arrays.toString(arguments));

        StopWatch stopWatch = new StopWatch();
        //
        Object retValue = null;
        //
        try {
            stopWatch.start();
            retValue = joinPoint.proceed();

        } catch(Exception ex) {
            //Exception
            ex.printStackTrace();

        } finally {
            stopWatch.stop();
            //
            long elapsed_time = stopWatch.getTotalTimeMillis();
            //
            if( elapsed_time > 1000 ) {

                System.out.println( Long.toString( time_stamp ) + " >> " + "[LAZY     ] "
                        + serviceMethodName + " : Method Proccess Stop (Elapsed Time : " + elapsed_time
                        + " msec) " + Arrays.toString(arguments));
            }
            //
            System.out.println( Long.toString( time_stamp ) + " >> " + "[Out      ] "
                    + serviceMethodName + " : Method Proccess Stop (Elapsed Time : " + elapsed_time + " msec)");
        }

        return retValue;
    }

}
