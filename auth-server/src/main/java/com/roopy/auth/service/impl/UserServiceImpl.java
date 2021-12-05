package com.roopy.auth.service.impl;

import com.roopy.auth.dto.UserDto;
import com.roopy.auth.entity.Authority;
import com.roopy.auth.entity.User;
import com.roopy.auth.repository.UserRepository;
import com.roopy.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public User createUser(UserDto userDto) {
        if (userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 사용자 입니다.");
        }

        // 사용자정보 설정
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        return userRepository.save(user);
    }

    @Override
    public User findUserByUsername(String username) {
        User user = userRepository.findOneWithAuthoritiesByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User Not Found with -> username or email: " + username));

        return user;
    }
}