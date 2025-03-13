package com.hufs_cheongwon.repository;

import com.hufs_cheongwon.domain.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlackListRepository extends JpaRepository<BlackList, Long> {

    boolean existsByAccessToken(String token);
}
