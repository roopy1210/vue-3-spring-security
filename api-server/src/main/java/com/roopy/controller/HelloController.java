package com.roopy.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class HelloController {

    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Map<String,String>> admin(HttpServletRequest httpServletRequest
            , HttpServletResponse httpServletResponse) throws Exception {

        Map<String,String> retObj = new HashMap<>();
        retObj.put("msg","Admin Page!!!!");

        return new ResponseEntity<>(retObj, HttpStatus.OK);
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Map<String,String>> user(HttpServletRequest httpServletRequest
            , HttpServletResponse httpServletResponse) throws Exception {

        Map<String,String> retObj = new HashMap<>();
        retObj.put("msg","User Page!!!");

        return new ResponseEntity<>(retObj, HttpStatus.OK);
    }

    @GetMapping("/greeting")
    public ResponseEntity<String> sayHello() throws Exception {
        return new ResponseEntity<>("Hello!!!", HttpStatus.OK);
    }
}
