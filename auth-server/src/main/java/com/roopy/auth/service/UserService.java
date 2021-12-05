package com.roopy.auth.service;

import com.roopy.auth.dto.UserDto;
import com.roopy.auth.entity.User;

public interface UserService {
    /**
     * 신규 사용자 등록
     *
     * @param userDto 사용자등록 정보
     * @return User 사용자등록 정보
     */
    public User createUser(UserDto userDto);

    /**
     * 사용자정보 조회
     *
     * @param username 사용자이메일
     * @return User 사용자정보
     */
    public User findUserByUsername(String username);
}
