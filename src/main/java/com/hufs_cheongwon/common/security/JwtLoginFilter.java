package com.hufs_cheongwon.common.security;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.naming.AuthenticationException;

@RequiredArgsConstructor
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {

//    private final AuthenticationManager authenticationManager;
//    private JWTUtil jwtUtil;
//
//    @Override
//    public Authentication attempAuthentication(HttpServletRequest request, HttpServletResponse response)
//            throws AuthenticationException {
//
//    }
}
