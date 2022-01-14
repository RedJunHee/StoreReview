package com.review.storereview.controller.cust;

import com.review.storereview.common.JwtTokenProvider;
import com.review.storereview.common.enumerate.ApiStatusCode;
import com.review.storereview.common.exception.dto.ExceptionResponseDto;
import com.review.storereview.dto.ResponseJsonObject;
import com.review.storereview.dto.request.TokenDto;
import com.review.storereview.dto.request.UserSigninRequestDto;
import com.review.storereview.filter.AuthorizationCheckFilter;
import com.review.storereview.service.cust.BaseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
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
        Authentication authentication = null ;

        try {
            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        }catch(AuthenticationException ex)  // 인증 절차 실패시 리턴되는 Exception
        {
            return new ResponseEntity<>(ExceptionResponseDto.createMetaDto(ApiStatusCode.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
        }catch(Exception ex)
        {
            return new ResponseEntity<>(ExceptionResponseDto.createMetaMessageDto(ApiStatusCode.SYSTEM_ERROR,ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createTokenFromAuthentication(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AuthorizationCheckFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new ResponseJsonObject(ApiStatusCode.OK).setData(new TokenDto(jwt)), httpHeaders, HttpStatus.OK);
    }

}
