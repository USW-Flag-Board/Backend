package com.Flaground.backend.module.member.domain.repository;

import com.Flaground.backend.module.member.controller.dto.response.LoginLogResponse;
import com.Flaground.backend.module.member.controller.dto.response.MyProfileResponse;
import com.Flaground.backend.module.member.controller.dto.response.SearchMemberResponse;
import com.Flaground.backend.module.member.domain.Member;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> getDeactivateMembers();

    List<Member> getMembersByLoginIds(List<String> loginIdList);

    List<String> getDeactivableMemberEmails();

    MyProfileResponse getMyProfile(Long memberId);

    List<LoginLogResponse> getLoginLogs();

    List<SearchMemberResponse> searchMemberByName(String name);
}
