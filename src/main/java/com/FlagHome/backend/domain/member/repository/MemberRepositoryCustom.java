package com.FlagHome.backend.domain.member.repository;

import com.FlagHome.backend.domain.member.entity.Member;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepositoryCustom {
    List<Member> getAllSleepMembers();

    List<Member> getMembersByLoginId(List<String> loginIdList);
}
