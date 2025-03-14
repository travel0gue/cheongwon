package com.hufs_cheongwon.common.security;

import com.hufs_cheongwon.common.exception.AuthenticationException;
import com.hufs_cheongwon.common.exception.UserNotFoundException;
import com.hufs_cheongwon.domain.Admin;
import com.hufs_cheongwon.domain.Users;
import com.hufs_cheongwon.repository.AdminRepository;
import com.hufs_cheongwon.repository.BlackListRepository;
import com.hufs_cheongwon.repository.UsersRepository;
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
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Builder
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final AuthenticationException authenticationException;
    private final BlackListRepository blackListRepository;
    private final UsersRepository usersRepository;
    private final AdminRepository adminRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //TODO: 오류 처리 리팩토링
        try {
            //토큰 추출
            String token = jwtUtil.resolveAccessToken(request);
            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            //토큰 유효기간 확인
            if (jwtUtil.isExpired(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            //로그아웃된 토큰인지 확인
            boolean isLogoutToken = blackListRepository.existsByAccessToken(token);
            if (isLogoutToken) {
                throw new IllegalAccessException("로그아웃된 토큰입니다.");
            }
            System.out.println("islougoutToken? "+isLogoutToken);
            String username = jwtUtil.getUsername(token);
            String role = jwtUtil.getRole(token);
            System.out.println(role);

            //역할 별로 Custom User/Admin Details 만들기
            if (role.equals("ROLE_USER")) {

                SecurityContextHolder.clearContext();
                Optional<Users> user = usersRepository.findByEmail(username);
                CustomUserDetails customUserDetails = CustomUserDetails.from(user.get());
                Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
                filterChain.doFilter(request, response);
            } else if (role.equals("ROLE_ADMIN")) {

                SecurityContextHolder.clearContext();
                Optional<Admin> admin = adminRepository.findByEmail(username);
                CustomAdminDetails customAdminDetails = CustomAdminDetails.from(admin.get());
                Authentication authToken = new UsernamePasswordAuthenticationToken(customAdminDetails, null, customAdminDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);

                filterChain.doFilter(request, response);
            }
        } catch (ExpiredJwtException e) {
            SecurityContextHolder.clearContext();
            authenticationException.sendErrorResponse(response, ErrorStatus.TOKEN_EXPIRATION);
        } catch (IllegalAccessException e) {
          SecurityContextHolder.clearContext();
          authenticationException.sendErrorResponse(response, ErrorStatus.BLACK_LIST_TOKEN);
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            authenticationException.sendErrorResponse(response, ErrorStatus.TOKEN_INVALID);
        }
    }
}
