package com.Flaground.backend.module.member.domain.repository;

public interface BlackListRepositoryCustom {
    boolean existsByEmail(String email);

    void releaseBannedMembers();
}
