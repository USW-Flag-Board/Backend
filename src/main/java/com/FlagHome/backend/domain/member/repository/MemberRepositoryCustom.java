package com.FlagHome.backend.domain.member.repository;

import com.FlagHome.backend.domain.member.controller.dto.LoginLogResponse;
import com.FlagHome.backend.domain.member.Member;
import com.FlagHome.backend.domain.member.controller.dto.SearchMemberResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepositoryCustom {
    List<Member> getAllSleepMembers();

    List<Member> getMembersByLoginIdList(List<String> loginIdList);

    List<String> getAllBeforeSleepEmails();

    List<LoginLogResponse> getAllLoginLogs();

    List<SearchMemberResponse> getSearchResultsByName(String name);
}
