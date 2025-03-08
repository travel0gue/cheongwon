package com.hufs_cheongwon.common.security;

import com.hufs_cheongwon.common.exception.AuthenticationException;
import com.hufs_cheongwon.domain.Admin;
import com.hufs_cheongwon.domain.Users;
import com.hufs_cheongwon.web.apiResponse.error.ErrorStatus;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Builder
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final AuthenticationException authenticationException;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authorization = request.getHeader("Authorization");

            if (authorization == null || !authorization.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            System.out.println("jwt필터 들어옴?");
            System.out.println(authorization);
            String token = authorization.split(" ")[1];
            if (jwtUtil.isExpired(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            String username = jwtUtil.getUsername(token);
            String role = jwtUtil.getRole(token);
            System.out.println(role);

            if (role.equals("ROLE_USER")) {

                System.out.println("필터에서 역할이 유저일 때");
                SecurityContextHolder.clearContext();
                Users user = Users.builder()
                        .email(username)
                        .password("temppassword")
                        .build();
                CustomUserDetails customUserDetails = CustomUserDetails.from(user);
                Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
                filterChain.doFilter(request, response);
            } else if (role.equals("ROLE_ADMIN")) {

                SecurityContextHolder.clearContext();
                Admin admin = Admin.builder()
                        .email(username)
                        .password("temppassword")
                        .build();
                CustomAdminDetails customAdminDetails = CustomAdminDetails.from(admin);
                Authentication authToken = new UsernamePasswordAuthenticationToken(customAdminDetails, null, customAdminDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);

                filterChain.doFilter(request, response);
            }
        } catch (ExpiredJwtException e) {
            SecurityContextHolder.clearContext();
            authenticationException.sendErrorResponse(response, ErrorStatus.TOKEN_EXPIRATION);
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            authenticationException.sendErrorResponse(response, ErrorStatus.TOKEN_INVALID);
        }
    }
}
