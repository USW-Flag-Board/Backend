package com.FlagHome.backend.domain.member.avatar.repository;

import com.FlagHome.backend.domain.member.avatar.entity.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Long>, AvatarRepositoryCustom {
    Optional<Avatar> findByMemberId(long memberId);
}
