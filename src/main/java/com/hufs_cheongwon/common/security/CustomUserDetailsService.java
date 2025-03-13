package com.hufs_cheongwon.common.security;

import com.hufs_cheongwon.common.exception.UserNotFoundException;
import com.hufs_cheongwon.domain.Users;
import com.hufs_cheongwon.repository.UsersRepository;
import com.hufs_cheongwon.web.apiResponse.error.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;

    //로그인된 사용자의 정보를 CustomUserDetails에 담아서 반환하는 역할
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("커스텀 유저 디테일");

        Users user = usersRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException(ErrorStatus.USER_NOT_FOUND));

        return CustomUserDetails.from(user);
    }
}
