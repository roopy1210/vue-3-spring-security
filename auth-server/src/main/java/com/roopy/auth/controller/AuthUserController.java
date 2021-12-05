package com.roopy.auth.controller;

import com.roopy.auth.dto.UserDto;
import com.roopy.auth.entity.User;
import com.roopy.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class AuthUserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto) {

        userService.createUser(userDto);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping("/{username}")
    public ResponseEntity<UserDto> findUidByUsername(@PathVariable String username) {
        User user = userService.findUserByUsername(username);

        // Return Object
        UserDto userDto = new UserDto();
        userDto.setNickname(user.getNickname());

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}
