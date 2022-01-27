package com.review.storereview.service.cms;

import com.review.storereview.common.enumerate.Authority;
import com.review.storereview.dao.JWTUserDetails;
import com.review.storereview.dao.cms.User;
import com.review.storereview.repository.cms.BaseUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Class       : AuthService
 * Author      : 조 준 희
 * Description : 사용자 인증/인가 서비스
 * History     : [2022-01-10] - 조 준희 - Class Create
 */

@Component
public class AuthService implements UserDetailsService {
    private final BaseUserRepository userRepository;

    @Autowired
    public AuthService(BaseUserRepository UserRepository) {
        this.userRepository = UserRepository;
    }

    /**
     * AuthenticationProvider가 호출하는 loadUserByUsername 오버라이딩 함수.
     * @param username
     * @return
     * @throws AuthenticationException
     */
    @Override
    public UserDetails loadUserByUsername(final String username) throws AuthenticationException {

        Optional<User> result = userRepository.findOneByUserId(username);

        return result.map(user -> createUser(username, user))
                .orElseThrow(() ->  new UsernameNotFoundException(username + " -> 일치하는 사용자가 없습니다..") );
    }
    /**
     * DTO.User객체를 UserDetails객체로 변환.
     * @param username
     * @param user
     * @return
     */
    private JWTUserDetails createUser(String username, User user) {
//        if (!user.isActivated()) {
//            throw new RuntimeException(username + " -> 활성화되어 있지 않습니다.");
//        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();


        // 데이터베이스 권한을 가져와서 할당해주어야함.
        grantedAuthorities.add(new SimpleGrantedAuthority(Authority.USER.getFullName()));

        return new JWTUserDetails(user.getUserId(),
                user.getPassword(),
                grantedAuthorities,user.getSuid(), user.getSaid());
    }
}
