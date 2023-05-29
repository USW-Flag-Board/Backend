package com.Flaground.backend.module.member.domain.repository;

import com.Flaground.backend.module.member.domain.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlackListRepository extends JpaRepository<BlackList, Long>, BlackListRepositoryCustom {
    Optional<BlackList> findByEmail(String email);
}
