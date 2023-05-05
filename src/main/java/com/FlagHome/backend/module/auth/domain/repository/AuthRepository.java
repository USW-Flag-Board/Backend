package com.FlagHome.backend.module.auth.domain.repository;

import com.FlagHome.backend.module.auth.domain.AuthInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<AuthInformation, Long>, AuthRepositoryCustom {
    Optional<AuthInformation> findByEmail(String email);

    Optional<AuthInformation> findById(Long authInformationId);

    void deleteById(Long authInformationId);

    Optional<AuthInformation> findFirstByEmailOrderByCreatedAtDesc(String email);
}
