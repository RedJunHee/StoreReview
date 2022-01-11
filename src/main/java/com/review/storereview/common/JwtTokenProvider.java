package com.review.storereview.common;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class       : JwtTokenProvider
 * Author      : 조 준 희
 * Description : JWT 토큰 공급자 객체.
 * History     : [2022-01-10] - 조 준희 - Class Create
 */
@Component
public class JwtTokenProvider implements AuthenticationProvider {

    private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";
    private final String secret;
    private final long tokenValidityInMilliseconds;
    private Key key;
    private final UserDetailsService userDetailsService;


    @Autowired
    public JwtTokenProvider(@Value("${jwt.secret}")String secret,
                            @Value("${jwt.token-validity-in-seconds}")long tokenValidityInSeconds,
                            UserDetailsService userDetailsService) {
        this.secret = secret;
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.secret));
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Request 받은 Authentication(Login)이 인증유저인지 확인하는 인증절차.
     * @param authentication
     * @return 인증 토큰 생성
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        UserDetails userDetails = null ;
        try {
            // Service에서 패스워드비교까지 모두 처리.
            userDetails = userDetailsService.loadUserByUsername(username);
        }
        catch(UsernameNotFoundException ex)
        {
            throw ex;
        }
        return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
    }


    /**
     *  authentication 타입이 UsernamePasswordAuthentiacationToken인 경우만 지원하는 필터 명시
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }


    /** Authentication정보로 토큰 생성
     * @param authentication
     * @return AccessToken
     */
    public String createTokenFromAuthentication(Authentication authentication)
    {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);

        // Payload
        Map<String,Object> payload = new HashMap<String,Object>();
        payload.put("data","asd");


        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .claim("data","SimpleData!!")
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }


    /**
     * 토큰으로 Authentication 객체 생성
     * @param token
     * @return
     */
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /**
     * Token 벨리테이션
     * @param token
     * @return
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

}
