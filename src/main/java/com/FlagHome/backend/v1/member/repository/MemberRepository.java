package com.FlagHome.backend.v1.member.repository;

import com.FlagHome.backend.v1.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, CustomMemberRepository {
    Optional<Member> findById(Long memberId);

    Optional<Member> findByLoginId(String loginId);
    
    boolean existsByLoginId(String loginId);

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);
}
