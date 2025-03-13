package com.hufs_cheongwon.repository;

import com.hufs_cheongwon.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {

    Boolean existsByEmail(String email);

    Optional<Users> findByEmail(String email);

    void deleteByEmail(String email);
}
