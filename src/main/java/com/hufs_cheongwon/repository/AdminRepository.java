package com.hufs_cheongwon.repository;

import com.hufs_cheongwon.domain.Admin;
import com.hufs_cheongwon.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long>{

    Boolean existsByEmail(String email);

    Optional<Admin> findByEmail(String email);
}
