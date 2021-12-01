package com.roopy.controller;

import com.roopy.dto.UserDto;
import com.roopy.jwt.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> createUser(HttpServletRequest httpServletRequest
            , HttpServletResponse httpServletResponse
            , @Valid @RequestBody UserDto userDto) throws Exception {

        HttpEntity<UserDto> request = new HttpEntity<>(userDto);
        ResponseEntity<UserDto> responseEntity = restTemplate.postForEntity( "http://localhost:10001/user/register", request , UserDto.class);

        return new ResponseEntity<>(responseEntity.getBody(), HttpStatus.OK);
    }
}
