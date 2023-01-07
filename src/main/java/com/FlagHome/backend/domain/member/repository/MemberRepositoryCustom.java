package com.FlagHome.backend.domain.member.repository;

import com.FlagHome.backend.domain.member.dto.UpdateProfileRequest;
import com.FlagHome.backend.domain.member.entity.Member;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepositoryCustom {

    void updateProfile(Long memberId, UpdateProfileRequest updateProfileRequest);

    List<Member> getAllSleepMembers();

}
