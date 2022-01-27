package com.review.storereview.common;

import com.review.storereview.common.utils.CryptUtils;
import com.review.storereview.dao.JWTUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private static final String AUTHORITIES_KEY = "auth";   // 사용자 권한 체크 위함

    private final String secret;
    private final long tokenExpiryInMilliseconds;
    private Key key;

    private final UserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;
    private final CryptUtils cryptUtils;
    @Autowired
    public JwtTokenProvider(@Value("${jwt.secret}")String secret,
                            @Value("${jwt.token-validity-in-seconds}")long tokenExpiryInSeconds,
                            UserDetailsService userDetailsService,
                            PasswordEncoder passwordEncoder,
                            CryptUtils cryptUtils) {
        this.secret = secret;
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.secret));
        this.tokenExpiryInMilliseconds = tokenExpiryInSeconds * 1000;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.cryptUtils = cryptUtils;
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
        try {
            // Service에서 패스워드비교까지 모두 처리.
            // null일경우 UsernameNotFoundException Throw
            JWTUserDetails userDetails = (JWTUserDetails)(userDetailsService.loadUserByUsername(username));

            passwordChecks(userDetails, (UsernamePasswordAuthenticationToken) authentication);

            return new JWTAuthenticationToken(username, password, userDetails.getAuthorities(),userDetails.getSuid(),userDetails.getSaid());
        }
        catch(UsernameNotFoundException ex)
        {
            throw ex;
        }
    }
    /**
     * 인증 절차 - 비밀번호 체크
     * @param userDetails 데이터 베이스에서 검색한 유저의 정보
     * @param authentication 인증 요청한 요청 정보
     * @throws AuthenticationException
     */
    private void passwordChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException
    {
        if (authentication.getCredentials() == null) {
            this.logger.debug("Failed to authenticate since no credentials provided");
//            throw new BadCredentialsException(this.messages
//                    .getMessage("JwtTokenProvider.badCredentials", "Bad credentials"));
            throw new BadCredentialsException("Bad credentials");
        }
        String presentedPassword = authentication.getCredentials().toString();
        if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            this.logger.debug("Failed to authenticate since password does not match stored value");
//            throw new BadCredentialsException(this.messages
//                    .getMessage("JwtTokenProvider.badCredentials", "Bad credentials"));
            throw new BadCredentialsException("Bad credentials");
        }

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
    public String createTokenFromAuthentication(JWTAuthenticationToken authentication) throws Exception
    {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenExpiryInMilliseconds);

        String jwt ="";
        try {
            jwt = Jwts.builder()
                    .setSubject(authentication.getName())
                    .claim(AUTHORITIES_KEY, authorities)
                    .claim("suid",  authentication.getSuid())
                    .claim("said", authentication.getSaid())
                    //.claim("suid", cryptUtils.getAES().encrypt(cryptUtils.getSecretKey(), authentication.getSuid()))
                    //.claim("said", cryptUtils.getAES().encrypt(cryptUtils.getSecretKey(), authentication.getSaid()))
                    .signWith(key, SignatureAlgorithm.HS512)
                    .setExpiration(validity)
                    .compact();
        }catch(Exception e) {
            logger.debug("JWT Token 생성 Exception "+ e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return jwt;
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
