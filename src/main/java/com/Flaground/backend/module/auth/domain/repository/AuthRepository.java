package com.Flaground.backend.module.auth.domain.repository;

import com.Flaground.backend.module.auth.domain.AuthInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<AuthInformation, Long>, AuthRepositoryCustom {
    Optional<AuthInformation> findByEmail(String email);

    Optional<AuthInformation> findById(Long authInformationId);

    void deleteById(Long authInformationId);

    Optional<AuthInformation> findFirstByEmailOrderByCreatedAtDesc(String email);
}
