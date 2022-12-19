package com.FlagHome.backend.v1.member.service;

import com.FlagHome.backend.v1.member.dto.UpdateRequest;
import com.FlagHome.backend.v1.member.repository.MemberRepository;
import com.FlagHome.backend.v1.util.SecurityUtils;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Member;
import java.net.PasswordAuthentication;

import static com.FlagHome.backend.v1.member.entity.QMember.member;
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final JPAQueryFactory queryFactory;
    private final MemberRepository memberRepository;
    private  final PasswordEncoder passwordEncoder;

    public void
    @Transactional
    public void update(UpdateRequest updateRequest) {

         queryFactory
                .update(member)
                .set(member.email,updateRequest.getEmail())
                .set(member.bio,updateRequest.getBio())
                .set(member.phoneNumber,updateRequest.getPhoneNumber())
                .where(member.id.eq(SecurityUtils.getMemberId()))
                .execute();
    }

}