package com.hufs_cheongwon.common.security;

import com.hufs_cheongwon.common.exception.UserNotFoundException;
import com.hufs_cheongwon.domain.Admin;
import com.hufs_cheongwon.repository.AdminRepository;
import com.hufs_cheongwon.web.apiResponse.error.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomAdminDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("어드민 유저 디테일");

        Admin admin = adminRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException(ErrorStatus.ADMIN_NOT_FOUND));

        return CustomAdminDetails.from(admin);
    }
}
