package com.roopy.api.config;

import com.roopy.api.jwt.JwtAccessDeniedHandler;
import com.roopy.api.jwt.JwtAuthenticationEntryPoint;
import com.roopy.api.jwt.JwtSecurityConfig;
import com.roopy.api.jwt.JwtUtil;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.client.RestTemplate;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityApiConfig extends WebSecurityConfigurerAdapter {

    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public SecurityApiConfig(JwtUtil jwtUtil, RestTemplate restTemplate,
                             JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAccessDeniedHandler jwtAccessDeniedHandler) {
        this.jwtUtil = jwtUtil;
        this.restTemplate = restTemplate;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(
                        "/h2-console/**"
                        ,"/favicon.ico"
                        ,"/api/test/greeting"
                        ,"/signin"
                        ,"/signout"
                        ,"/user/signup"
                        ,"/v2/api-docs"
                        ,"/configuration/ui"
                        ,"/swagger-resources/**"
                        ,"/configuration/security"
                        ,"/swagger-ui.html"
                        ,"/webjars/**"
                        ,"/token/**"
                );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // token??? ???????????? ???????????? ????????? csrf??? disable?????????.
                .csrf().disable()

                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // enable h2-console
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                // ????????? ???????????? ?????? ????????? STATELESS??? ??????
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/api/test/admin").permitAll()
                .antMatchers("/api/test/user").permitAll()

                .anyRequest().authenticated()

                .and()
                .apply(new JwtSecurityConfig(jwtUtil, restTemplate));
    }
}
