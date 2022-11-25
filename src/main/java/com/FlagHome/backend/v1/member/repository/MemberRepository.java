package com.FlagHome.backend.v1.member.repository;

import com.FlagHome.backend.v1.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByLoginId(String LoginId);
    boolean existsByLoginId(String LoginId);
}
