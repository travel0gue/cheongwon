package com.hufs_cheongwon.common.security;

import com.hufs_cheongwon.common.Util;
import com.hufs_cheongwon.common.exception.ResourceNotFoundException;
import com.hufs_cheongwon.domain.Admin;
import com.hufs_cheongwon.repository.AdminRepository;
import com.hufs_cheongwon.web.apiResponse.error.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomAdminDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("[Authentication] 관리자 CustomAdminDetailsService - 입력 이메일: {}", Util.maskEmail(username));
        Admin admin = adminRepository.findByEmail(username)
                .orElseThrow(() ->{
                        log.warn("[Authentication] 관리자 정보 없음 - 이메일: {}", Util.maskEmail(username));
                        return new ResourceNotFoundException(ErrorStatus.ADMIN_NOT_FOUND);
                });

        return CustomAdminDetails.from(admin);
    }
}
