package com.review.storereview.service.log;

import com.review.storereview.dao.log.Api_Log;
import com.review.storereview.repository.log.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/** Class       : LogService (Service)
 *  Author      : 조 준 희
 *  Description : 로그 관련 서비스 객체  ( Interface의 필요성 아직 모르겠어서 없음. )
 *  History     : [2022-01-03] - Temp
 */
@Service
public class LogService {

    private final LogRepository logRepository;

    @Autowired
    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }


    public boolean InsertApiLog(Api_Log logData){
        try{
            Api_Log data = logRepository.save(logData);

            if(data != null)
                return true;
            else
                return false;

        }catch (Exception ex) {
            throw ex;
        }
}
}
