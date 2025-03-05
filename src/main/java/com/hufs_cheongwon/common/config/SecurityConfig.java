package com.hufs_cheongwon.common.config;

import com.hufs_cheongwon.common.security.JwtLoginFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

//    private final AuthenticationConfiguration authenticationConfiguration;
//    private final JWTUtil jwtUtil;
//
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf((auth) -> auth.disable())
                .formLogin((auth) -> auth.disable())
                .httpBasic((auth) -> auth.disable())
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
                .authorizeHttpRequests((auth) -> auth
                        .anyRequest().permitAll());

        return http.build();
    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
//        return configuration.getAuthenticationManager();
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        //csrf, Form 로그인 방식, http basic 인증 방식 disable (세션 방식이 아닌 jwt 방식 로그인을 사용하기 때문)
//        http
//                .csrf((auth) -> auth.disable())
//                .formLogin((auth) -> auth.disable())
//                .httpBasic((auth) -> auth.disable())
//                .sessionManagement((session) -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//        //경로별 인가 작업
//        http
//                .authorizeHttpRequests((auth) -> auth
//                        .requestMatchers("/user/login", "/user/signup", "admin/login").permitAll()
//                        .requestMatchers("/admin").hasRole("ADMIN")
//                        .anyRequest().authenticated());
//
//        // addFilterAt == 추가할 필터, 대체될 필터
//        // 로그인 필터를 jwt 로그인 필터로 대체
//        JwtLoginFilter jwtLoginFilter = new JwtLoginFilter(
//                authenticationManager(authenticationConfiguration),
//                jwtUtil
//        );
//        jwtLoginFilter.setFilterProcessesUrl("/user/login")
//        http
//                .addFilterAt(jwtLoginFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
}
