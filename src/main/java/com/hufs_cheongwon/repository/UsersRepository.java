package com.hufs_cheongwon.repository;

import com.hufs_cheongwon.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
}
