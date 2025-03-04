package com.hufs_cheongwon.service;

import com.hufs_cheongwon.common.exception.DuplicateResourceException;
import com.hufs_cheongwon.domain.Users;
import com.hufs_cheongwon.domain.enums.Role;
import com.hufs_cheongwon.domain.enums.Status;
import com.hufs_cheongwon.repository.UsersRepository;
import com.hufs_cheongwon.web.apiResponse.error.ErrorStatus;
import com.hufs_cheongwon.web.dto.UserJoinRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UsersService {

    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void registerUser(UserJoinRequest request) {

        String email = request.getEmail();
        String password = request.getPassword();

        Boolean isExist = usersRepository.existsByEmail(email);

        if (isExist){
            throw new DuplicateResourceException(ErrorStatus.EMAIL_DUPLICATED);
        }

        Users user = Users.builder()
                .email(email)
                .role(Role.ROLE_USER)
                .status(Status.ACTIVE)
                .build();

        user.setEncodedPassword(bCryptPasswordEncoder.encode(password));
        usersRepository.save(user);
    }
}
