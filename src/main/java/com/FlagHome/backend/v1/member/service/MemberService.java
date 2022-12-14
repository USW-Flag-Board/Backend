package com.FlagHome.backend.v1.member.service;

import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.FlagHome.backend.v1.member.entity.Member;
import com.FlagHome.backend.v1.member.repository.MemberRepository;
import com.FlagHome.backend.v1.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;


    @Transactional
    public void updateMember(@NotNull UserDto userDto) {
        Member memberEntity = MemberRepository.findByLoginId(userDto.getId());
        .setPassword(userDto.getPassword());
        .setName(userDto.getName());
        .setPhoneNumber(userDto.getPhoneNumber());
        .setMajor(userDto.getMajor());
    }
}