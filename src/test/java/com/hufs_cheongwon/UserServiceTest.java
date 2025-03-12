package com.hufs_cheongwon;

import com.hufs_cheongwon.common.exception.DuplicateResourceException;
import com.hufs_cheongwon.domain.Users;
import com.hufs_cheongwon.repository.UsersRepository;
import com.hufs_cheongwon.service.UsersService;
import com.hufs_cheongwon.web.apiResponse.error.ErrorStatus;
import com.hufs_cheongwon.web.dto.request.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UsersService userCommandService;

    @BeforeEach
    void setUp() {
        userCommandService = new UsersService(usersRepository, bCryptPasswordEncoder);
    }

    @Test
    public void 회원가입() throws Exception {

        // given
        LoginRequest request = new LoginRequest("test@example.com", "password123");

        when(usersRepository.existsByEmail(request.getEmail())).thenReturn(false); // 이메일 중복 없음
        when(bCryptPasswordEncoder.encode(request.getPassword())).thenReturn("encodedPassword123"); // 암호화된 비밀번호

        // when
        userCommandService.registerUser(request);

        // then
        verify(usersRepository, times(1)).save(any(Users.class)); // 한 번만 save 호출되어야 함

    }

    @Test
    void 회원가입_이메일_중복_예외() {
        // given
        LoginRequest request = new LoginRequest("test@example.com", "password1232!");

        when(usersRepository.existsByEmail(request.getEmail())).thenReturn(true); // 이메일 중복 발생

        // when & then
        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> {
            userCommandService.registerUser(request);
        });

        assertEquals(ErrorStatus.EMAIL_DUPLICATED, exception.getErrorStatus());
    }
}
