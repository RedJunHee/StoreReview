package com.review.storereview.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Class       : TestController
 * Author      : 조 준 희
 * Description : Class Description
 * History     : [2022-01-12] - 조 준희 - Class Create
 */
@RestController
@RequestMapping("/test")
public class TestController {

    /**
     * test용 api
     * @return
     */
    @GetMapping("/tester")
    public ResponseEntity<String> tester(){
        return new ResponseEntity<>( "testAPI", HttpStatus.OK);
    }

    /**
     * test용 api
     * @return
     */
    @GetMapping("/admin")
    public ResponseEntity<String> admin(){
        return new ResponseEntity<>( "testAPI", HttpStatus.OK);
    }

}
