package com.FlagHome.backend.domain.auth.repository;

import com.FlagHome.backend.domain.auth.entity.AuthInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<AuthInformation, Long>, AuthRepositoryCustom {
    Optional<AuthInformation> findByEmail(String email);

    Optional<AuthInformation> findById(Long authMemberId);

    void deleteById(Long authMemberId);

    Optional<AuthInformation> findFirstByEmailOrderByCreatedAtDesc(String email);
}
