package com.roopy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roopy.crypto.AES256Cipher;
import com.roopy.dto.UserDto;
import com.roopy.jwt.JwtUtil;
import com.roopy.security.jwt.payload.request.LoginRequest;
import com.roopy.security.jwt.payload.response.TokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 로그인
     *
     * <p>
     *     1.인증서버에 AccessToken, RefreshToken 발급요청
     *     2.발급된 토큰 정보에 대한 쿠키 생성
     * </p>
     *
     *
     * @param httpServletRequest HttpServletRequest
     * @param httpServletResponse HttpServletResponse
     * @param loginRequest set username and password
     * @return TokenResponse accessToken
     * @throws Exception exception
     */
    @PostMapping("/signin")
    public ResponseEntity<TokenResponse> signin(HttpServletRequest httpServletRequest
            , HttpServletResponse httpServletResponse
            , @Valid @RequestBody LoginRequest loginRequest) throws Exception {

        // 토큰 발행 요청
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper mapper = new ObjectMapper();

        HttpEntity<String> request = new HttpEntity<>(mapper.writeValueAsString(loginRequest), headers);
        ResponseEntity<HashMap> tokenIssueResponseEntity = restTemplate.postForEntity( "http://localhost:10001/token/issue", request , HashMap.class );

        // 토큰 발행 요청 결과
        String accessToken = null;

        TokenResponse tokenResponse = null;

        if (tokenIssueResponseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
            accessToken = (String) tokenIssueResponseEntity.getBody().get("accessToken");

            Authentication authentication = jwtUtil.getAuthentication(accessToken);

            User user = (User) authentication.getPrincipal();
            List<String> roles = new ArrayList<>();
            for (GrantedAuthority item : user.getAuthorities()) {
                String authority = item.getAuthority();
                roles.add(authority);
            }

            // 사용자 추가 정보 조회를 위해 사용자정보 조회
            ResponseEntity<UserDto> userResponseEntity = restTemplate.postForEntity( "http://localhost:10001/user/" + loginRequest.getUsername()
                    ,loginRequest
                    , UserDto.class );

            String nickName = null == userResponseEntity.getBody() ? "" : userResponseEntity.getBody().getNickname();

            tokenResponse = TokenResponse.builder()
                    .accessToken(accessToken)
                    .username(user.getUsername())
                    .displayName(nickName)
                    .roles(roles)
                    .build();
        }

        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
    }

    /**
     * 로그아웃
     *
     * @param httpServletRequest HttpServletRequest
     * @param httpServletResponse HttpServletResponse
     * @return 처리결과 메세지
     * @throws Exception exception
     */
    @PostMapping("/signout")
    public ResponseEntity<String> signout(HttpServletRequest httpServletRequest
            , HttpServletResponse httpServletResponse
            , @RequestBody HashMap<String,String> param) throws Exception {

        String username = AES256Cipher.encrypt(param.get("username"));
        logger.debug("{} try to Logoout", username);

        HttpEntity<String> request = new HttpEntity<>(username);
        ResponseEntity<String> userResponseEntity = restTemplate.postForEntity( "http://localhost:10001/token/remove/refresh-token"
                , request
                , String.class );

        return new ResponseEntity<>("로그 아웃 처리 되었습니다.", HttpStatus.OK);
    }
}
