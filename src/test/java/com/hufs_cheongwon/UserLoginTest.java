package com.hufs_cheongwon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hufs_cheongwon.domain.Users;
import com.hufs_cheongwon.domain.enums.Status;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserLoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private final String USER_EMAIL = "user@example.com";
    private final String PASSWORD = "user1234";
    private final String WRONG_PASSWORD = "wrongpassword";

    @BeforeEach
    void setUp() {
        // 테스트용 사용자 계정 저장 (비밀번호 암호화 후 DB에 저장)
        Users user = Users.builder()
                .email(USER_EMAIL)
                .password(passwordEncoder.encode(PASSWORD))  // 비밀번호 암호화
                .name("User Test")
                .studentNumber("20230001")
                .status(Status.ACTIVE)
                .build();
        userRepository.save(user);
    }

    /**
     * 정상적인 사용자 로그인 테스트 (JWT 발급 확인)
     */
    @Test
    void 사용자_로그인_성공() throws Exception {
        // 요청 데이터
        String requestBody = objectMapper.writeValueAsString(new LoginRequest(USER_EMAIL, PASSWORD));

        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())  // HTTP 200 응답
                .andExpect(jsonPath("$.result.tokenDto.accessToken").exists())  // JWT Access Token 존재 여부 확인
                .andExpect(jsonPath("$.result.tokenDto.refreshToken").exists()); // JWT Refresh Token 존재 여부 확인
    }

    /**
     * 존재하지 않는 이메일로 로그인 시 실패
     */
    @Test
    void 존재하지_않는_이메일로_로그인_실패() throws Exception {
        String requestBody = objectMapper.writeValueAsString(new LoginRequest("wrong@example.com", PASSWORD));

        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized())  // HTTP 401 Unauthorized 응답
                .andExpect(jsonPath("$.message").value("존재하지 않는 유저입니다.")); // `USER_NOT_FOUND` 응답 확인
    }

    /**
     * 잘못된 비밀번호로 로그인 시 실패
     */
    @Test
    void 잘못된_비밀번호로_로그인_실패() throws Exception {
        String requestBody = objectMapper.writeValueAsString(new LoginRequest(USER_EMAIL, WRONG_PASSWORD));

        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized())  // HTTP 401 Unauthorized 응답
                .andExpect(jsonPath("$.message").value("잘못된 비밀번호입니다.")); // `PASSWORD_WRONG` 응답 확인
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


