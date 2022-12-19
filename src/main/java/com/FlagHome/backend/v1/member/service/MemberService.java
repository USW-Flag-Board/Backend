package com.FlagHome.backend.v1.member.service;

import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.FlagHome.backend.v1.Status;
import com.FlagHome.backend.v1.member.dto.WithdrawRequest;
import com.FlagHome.backend.v1.member.entity.Member;
import com.FlagHome.backend.v1.member.repository.MemberRepository;
import com.FlagHome.backend.v1.util.SecurityUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final JPAQueryFactory queryFactory;
    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public void withdrawMember(WithdrawRequest withdrawRequest){
        Member member = memberRepository.findById(SecurityUtils.getMemberId()).get();

        if (!passwordEncoder.matches(withdrawRequest.getPassword(), member.getPassword())){
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        memberRepository.withdraw(SecurityUtils.getMemberId());

    }
}