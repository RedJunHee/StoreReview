package com.review.storereview.service.cust;

import com.review.storereview.common.enumerate.Authority;
import com.review.storereview.dao.cust.User;
import com.review.storereview.repository.cust.BaseUserRepository;
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

        Optional<User> result = userRepository.findOneById(username);

        return result.map(user -> createUser(username, user))
                .orElseThrow(() ->  new UsernameNotFoundException(username + " -> 일치하는 사용자가 없습니다..") );
    }
    /**
     * DTO.User객체를 UserDetails객체로 변환.
     * @param username
     * @param user
     * @return
     */
    private org.springframework.security.core.userdetails.User createUser(String username, User user) {
//        if (!user.isActivated()) {
//            throw new RuntimeException(username + " -> 활성화되어 있지 않습니다.");
//        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();

        grantedAuthorities.add(new SimpleGrantedAuthority(Authority.TESTER.getFullName()));

        return new org.springframework.security.core.userdetails.User(user.getId(),
                user.getPassword(),
                grantedAuthorities);
    }
}
