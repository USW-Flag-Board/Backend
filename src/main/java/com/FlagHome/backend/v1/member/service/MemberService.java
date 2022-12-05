package com.FlagHome.backend.v1.member.service;

import com.FlagHome.backend.v1.member.dto.InitRequest;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.FlagHome.backend.v1.member.entity.QMember.member;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final JPAQueryFactory queryFactory;

    public void initMember(Long userId, InitRequest initRequest) {
        queryFactory
                .update(member)
                .set(member.email, initRequest.getEmail())
                .set(member.phoneNumber, initRequest.getPhoneNumber())
                .set(member.bio, initRequest.getBio())
                .where(member.id.eq(userId))
                .execute();
    }

}