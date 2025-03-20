package com.hufs_cheongwon.repository;

import com.hufs_cheongwon.domain.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResponseRepository extends JpaRepository<Response, Long> {

    @Override
    Page<Response> findAll(Pageable pageable);

    @Override
    Optional<Response> findById(Long id);

    Response findByPetitionId(Long id);

    Optional<Response> findOptionalByPetitionId(Long id);
}

