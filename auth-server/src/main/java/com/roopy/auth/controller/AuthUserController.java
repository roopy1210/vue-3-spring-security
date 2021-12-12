package com.roopy.auth.controller;

import com.roopy.api.jwt.JwtFilter;
import com.roopy.auth.dto.UserDto;
import com.roopy.auth.entity.User;
import com.roopy.auth.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class AuthUserController {

    private static final Logger logger = LoggerFactory.getLogger(AuthUserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto) {

        userService.createUser(userDto);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping("/{username}")
    public ResponseEntity<UserDto> findUidByUsername(@PathVariable String username) {
        logger.debug("username({})", username);
        User user = userService.findUserByUsername(username);
        logger.debug("user : {}", user.toString());

        // Return Object
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setNickname(user.getNickname());
        userDto.setUid(user.getUid());

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}
