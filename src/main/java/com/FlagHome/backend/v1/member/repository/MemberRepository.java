package com.FlagHome.backend.v1.member.repository;

import com.FlagHome.backend.v1.member.entity.Member;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.metamodel.SingularAttribute;
import java.io.Serializable;
import java.util.Optional;

@Transactional
@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, CustomMemberRepository {
    Optional<Member> findByLoginId(Long memberId);
    Optional<Member> findByLoginId(String LoginId);
    boolean existsByLoginId(String LoginId);
    @Query("update Member m set m.email = :email, m.bio = :bio, m.phoneNumber = :phoneNumber WHERE m.id = :memberId")
    void setUpdateRequest(
            @Param("memberId") Long id,
            @Param("email") String email,
            @Param("bio") String bio,
            @Param("phoneNumber") String phoneNumber
    );

}
