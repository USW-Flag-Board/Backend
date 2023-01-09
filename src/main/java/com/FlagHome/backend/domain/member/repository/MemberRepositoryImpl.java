package com.FlagHome.backend.domain.member.repository;

import com.FlagHome.backend.domain.member.dto.UpdateProfileRequest;
import com.FlagHome.backend.domain.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.FlagHome.backend.domain.member.entity.QMember.member;


@Repository
@Transactional
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Member> getAllSleepMembers() {
        return queryFactory
                .selectFrom(member)
                .where()
                .fetch();
    }
}
