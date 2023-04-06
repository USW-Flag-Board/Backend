package com.FlagHome.backend.domain.member.avatar.repository;

import com.FlagHome.backend.domain.member.avatar.entity.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar, Long>, AvatarRepositoryCustom {
    Optional<Avatar> findByMemberId(long memberId);
}
