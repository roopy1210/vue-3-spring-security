package com.roopy.api.jwt;

import com.roopy.api.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    // HEADER 에 Token 을 설정하는 경우 변수명
    public static final String AUTHORIZATION_HEADER = "Authorization";

    // Filter 클래스가 두번 호출 되는 것을 방지하기 위한 변수
    private static final String FILTER_APPLIED = "JwtFilterApplied";

    // AccessToken 사용자 정보 조회를 위한 클래스
    private JwtUtil jwtUtil;

    // AuthServer 와 연동시 사용하기 위한 클래스
    private RestTemplate restTemplate;

    // Token 사용자 정보를 위한 Key
    private static final String AUTHORITIES_KEY = "auth";

    public JwtFilter(JwtUtil jwtUtil, RestTemplate restTemplate) {
        this.jwtUtil = jwtUtil;
        this.restTemplate = restTemplate;
    }

    @Override
    public void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
            throws ServletException, IOException  {

        boolean isValidAccessToken = false;
        boolean isValidRefreshToken = false;

        // Filter 가 두번씨 호출 되는 부분을 처리하기 위한 로직
        if (httpServletRequest.getAttribute(FILTER_APPLIED) != null) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return ;
        }
        String requestURI = httpServletRequest.getRequestURI();

        // Request Header 에 전송된 accessToken
        String accessToken = parseJwt(httpServletRequest);

        // accessToken 유효성 체크
        if (null != accessToken) {
            isValidAccessToken = validateAccessToken(accessToken);
            logger.debug("AccessToken 유효성체크 : {}", isValidAccessToken);

            // TD-DO
            // accessToken 이 유효하지 않은 경우 RefreshToken 의 유효성을 체크한 후
            // 유효한 경우 accessToken 을 재발급 처리 한다.
            if (!isValidAccessToken) {
                String username = httpServletRequest.getHeader("username");
                logger.debug("uername({})", username);

                String refreshToken = null;

                if (null != username) {
                    // RefreshToken 조회
                    refreshToken = getRefreshToken(username);
                    logger.debug("RefreshToken : {}", refreshToken);

                    if (null != refreshToken) {
                        isValidRefreshToken = validateRefreshToken(refreshToken);
                        logger.debug("RefreshToken 유효성 체크 : {}", isValidRefreshToken);

                        // RefreshToken 이 유효한 경우 AccessToken 재발급
                       if (isValidRefreshToken) {
                            accessToken = reIssueAccessToken(refreshToken);

                            // AccessToken 유효성체크
                            isValidAccessToken = validateAccessToken(accessToken);
                            httpServletResponse.addHeader("accessToken", accessToken);
                       }
                    }
                }
            }
        }

        // AccessToken 이 유효한 경우 SecurityContextHolder 에 사용자 정보 설정
        Authentication authentication = null;

        logger.debug("AccessToken 유효성체크 : {}", isValidAccessToken);
        if (isValidAccessToken) {
            // 토큰 정보 조회
            authentication = getAuthentication(accessToken);

            // URL 권한 처리를 위해서 SecurityContextHolder 에 사용자 정보와 역할 정보를 설정
            // URL 에서 역할 설정은 @PreAuthorize("hasAnyRole('USER','ADMIN')") 통하여 제어 할 수 있다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        httpServletRequest.setAttribute(FILTER_APPLIED,true);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    /**
     * AccessToken 유효성 체크
     * 
     * @param accessToken accessToken
     * @return true or false
     */
    private Boolean validateAccessToken(String accessToken) {
        return jwtUtil.validateToken(accessToken);
    }

    /**
     * RefreshToken 조회
     *
     * @param username username
     */
    private String getRefreshToken(String username) {
        String refreshToken = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(username, headers);
        ResponseEntity<UserDto> responseEntity = restTemplate.postForEntity( "http://localhost:10001/user/" + username, request , UserDto.class);
        logger.debug("responseEntity: {}", responseEntity);

        refreshToken = responseEntity.getBody().getUid();

        return refreshToken;
    }

    /**
     * RefreshToken 유효성 체크
     *
     * @param refreshToken refreshToken
     * @return true or false
     */
    private boolean validateRefreshToken(String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(refreshToken, headers);
        ResponseEntity<Boolean> responseEntity = restTemplate.postForEntity( "http://localhost:10001/token/validate/refresh-token", request , Boolean.class);

        return responseEntity.getBody();
    }

    /**
     * AccessToken 재발급
     *
     * @param refreshToken refreshToken
     * @return refreshToken 재발급 AccessToken
     */
    private String reIssueAccessToken(String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(refreshToken, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity( "http://localhost:10001/token/re-issue/access-token", request , String.class);

        return responseEntity.getBody();
    }

    /**
     * 토근정보 조회
     *
     * @param accessToken accessToken
     * @return Authentication 사용자정보
     */
    private Authentication getAuthentication(String accessToken) {
        return jwtUtil.getAuthentication(accessToken);
    }

    /**
     * Header AccessToken 파싱
     *
     * @param request HttpServletRequest
     * @return accessToken
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }

        return null;
    }

}