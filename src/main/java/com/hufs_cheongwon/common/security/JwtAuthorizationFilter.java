package com.hufs_cheongwon.common.security;

import com.hufs_cheongwon.common.Util;
import com.hufs_cheongwon.common.exception.AuthenticationException;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
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
                log.info("[Authorization] 접근 토큰 없음 - URI: {}", request.getRequestURI());
                filterChain.doFilter(request, response);
                return;
            }

            //토큰 유효기간 확인
            if (jwtUtil.isExpired(token)) {
                log.warn("[Authorization] 토큰 만료됨 - URI: {}", request.getRequestURI());
                filterChain.doFilter(request, response);
                return;
            }

            //로그아웃된 토큰인지 확인
            boolean isLogoutToken = blackListRepository.existsByAccessToken(token);
            if (isLogoutToken) {
                log.warn("[Authorization] 로그아웃된 토큰으로 접근 시도 - URI: {}", request.getRequestURI());
                throw new IllegalAccessException("로그아웃된 토큰입니다.");
            }

            String username = jwtUtil.getUsername(token);
            String role = jwtUtil.getRole(token);
            log.info("[Authorization] 인증 시작 - 이메일: {}, 역할: {}", Util.maskEmail(username), role);

            //역할 별로 Custom User/Admin Details 만들기
            if (role.equals("ROLE_USER")) {

                SecurityContextHolder.clearContext();
                Optional<Users> user = usersRepository.findByEmail(username);
                CustomUserDetails customUserDetails = CustomUserDetails.from(user.get());
                Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.info("[Authorization] 사용자 인증 완료 - userId: {}, URI: {}", user.get().getId(), request.getRequestURI());
            } else {

                SecurityContextHolder.clearContext();
                Optional<Admin> admin = adminRepository.findByEmail(username);
                CustomAdminDetails customAdminDetails = CustomAdminDetails.from(admin.get());
                Authentication authToken = new UsernamePasswordAuthenticationToken(customAdminDetails, null, customAdminDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.info("[Authorization] 관리자 인증 완료 - adminId: {}, URI: {}", admin.get().getId(), request.getRequestURI());
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.warn("[Authorization] JWT 만료 예외 발생 - {}", e.getMessage());
            SecurityContextHolder.clearContext();
            authenticationException.sendErrorResponse(response, ErrorStatus.TOKEN_EXPIRATION);
        } catch (IllegalAccessException e) {
            log.warn("[Authorization] 블랙리스트 토큰 접근 차단 - {}", e.getMessage());
          SecurityContextHolder.clearContext();
          authenticationException.sendErrorResponse(response, ErrorStatus.BLACK_LIST_TOKEN);
        } catch (Exception e) {
            log.error("[Authorization] jwt 만료도, 블랙리스트 토큰도 아닌 인증 실패 - {}", e.getMessage());
            SecurityContextHolder.clearContext();
            authenticationException.sendErrorResponse(response, ErrorStatus.TOKEN_INVALID);
        }
    }
}
