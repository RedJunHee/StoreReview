package com.review.storereview.controller.cms;

import com.review.storereview.common.JWTAuthenticationToken;
import com.review.storereview.common.JwtTokenProvider;
import com.review.storereview.common.enumerate.ApiStatusCode;
import com.review.storereview.dto.ResponseJsonObject;
import com.review.storereview.dto.request.TokenDto;
import com.review.storereview.dto.request.UserSigninRequestDto;
import com.review.storereview.filter.AuthorizationCheckFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Class       : AuthController
 * Author      : 조 준 희
 * Description : 사용자 인증/인가 컨트롤러
 * History     : [2022-01-10] - 조 준희 - Class Create
 */
@RestController
public class AuthController {
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

   @Autowired
    public AuthController( JwtTokenProvider jwtTokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = jwtTokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }
    @PostMapping("/authenticate")
    public ResponseEntity<Object> authorize(@RequestBody UserSigninRequestDto loginDto){

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUserId(), loginDto.getPassword());
        JWTAuthenticationToken authentication = null ;
        String jwt = "";

        try {
            authentication = (JWTAuthenticationToken) authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            jwt = tokenProvider.createTokenFromAuthentication(authentication);
        }catch(AuthenticationException ex)  // 인증 절차 실패시 리턴되는 Exception
        {
            logger.debug("AuthController Auth 체크 실패 "+ ex.getMessage());
            ex.printStackTrace();
            return new ResponseEntity<>(ResponseJsonObject.withStatusCode(ApiStatusCode.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
        }catch(Exception ex)
        {
            ex.printStackTrace();
            return new ResponseEntity<>(ResponseJsonObject.withError(ApiStatusCode.SYSTEM_ERROR, ApiStatusCode.SYSTEM_ERROR.getType(), ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }   // 체크 필요!

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AuthorizationCheckFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(ResponseJsonObject.withStatusCode(ApiStatusCode.OK).setData(new TokenDto(jwt)), httpHeaders, HttpStatus.OK);
    }

}
