package com.Flaground.backend.module.member.domain.repository;

import com.Flaground.backend.global.common.SearchResponse;
import com.Flaground.backend.module.member.controller.dto.response.LoginLogResponse;
import com.Flaground.backend.module.member.controller.dto.response.MyProfileResponse;
import com.Flaground.backend.module.member.domain.Member;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> getDeactivateMembers();

    List<String> getDeactivableMemberEmails();

    MyProfileResponse getMyProfile(Long memberId);

    List<LoginLogResponse> getLoginLogs();

    SearchResponse searchMemberByName(String name);
}
