package com.hufs_cheongwon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hufs_cheongwon.common.security.JwtUtil;
import com.hufs_cheongwon.domain.Admin;
import com.hufs_cheongwon.domain.Users;
import com.hufs_cheongwon.domain.enums.Status;
import com.hufs_cheongwon.repository.AdminRepository;
import com.hufs_cheongwon.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AdminAuthenticationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private String userToken;
    private String adminToken;

    private final String ADMIN_EMAIL = "admin@example.com";
    private final String USER_EMAIL = "user@example.com";
    private final String PASSWORD = "user1234";

    @BeforeEach
    void setUp() {
        // 관리자 계정 저장
        Admin admin = Admin.builder()
                .email(ADMIN_EMAIL)
                .password(passwordEncoder.encode(PASSWORD))  // 비밀번호 암호화
                .build();
        adminRepository.save(admin);

        // 일반 사용자 계정 저장
        Users user = Users.builder()
                .email(USER_EMAIL)
                .password(passwordEncoder.encode(PASSWORD))
                .name("User Test")
                .studentNumber("20230001")
                .status(Status.ACTIVE)
                .build();
        userRepository.save(user);

        // JWT 토큰 생성
        userToken = jwtUtil.createAccessToken(USER_EMAIL, "ROLE_USER");
        adminToken = jwtUtil.createAccessToken(ADMIN_EMAIL, "ROLE_ADMIN");
    }

    /**
     * 유저는 관리자 로그인 불가
     */
    @Test
    void 일반_유저_관리자_로그인_실패() throws Exception {
        // 유저 계정으로 관리자 로그인 시도
        String requestBody = objectMapper.writeValueAsString(new LoginRequest(USER_EMAIL, PASSWORD));

        mockMvc.perform(post("/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized())  // HTTP 401 Unauthorized 응답
                .andExpect(jsonPath("$.message").value("관리자 계정을 찾을 수 없습니다.")); // `ADMIN_NOT_FOUND` 응답 확인
    }

    /**
     * 관리자는 유저 로그인 불가
     */
    @Test
    void 관리자_유저_로그인_실패() throws Exception {
        // 관리자 계정으로 유저 로그인 시도
        String requestBody = objectMapper.writeValueAsString(new LoginRequest(ADMIN_EMAIL, PASSWORD));

        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized())  // HTTP 401 Unauthorized 응답
                .andExpect(jsonPath("$.message").value("존재하지 않는 유저입니다.")); // `USER_NOT_FOUND` 응답 확인
    }

    /**
     * 로그인한 유저(`ROLE_USER`)가 관리자 API 접근 시 실패
     */
    @Test
    void 유저가_관리자_API_접근_실패() throws Exception {
        mockMvc.perform(get("/admin/test")  // 관리자 전용 API
                        .header("Authorization", "Bearer " + userToken))  // 일반 유저 토큰 사용
                .andExpect(status().isForbidden());  // HTTP 403 Forbidden 응답 확인
    }

    static class LoginRequest {
        public String email;
        public String password;

        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }
}

