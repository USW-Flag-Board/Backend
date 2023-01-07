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
    public void updateProfile(Long memberId, UpdateProfileRequest updateProfileRequest) {
        queryFactory
                .update(member)
                .set(member.major, updateProfileRequest.getMajor())
                .set(member.bio, updateProfileRequest.getBio())
                .set(member.phoneNumber, updateProfileRequest.getPhoneNumber())
                .set(member.studentId, updateProfileRequest.getStudentId())
                .where(member.id.eq(memberId))
                .execute();
    }

    @Override
    public List<Member> getAllSleepMembers() {
        return queryFactory
                .selectFrom(member)
                .where()
                .fetch();
    }
}
