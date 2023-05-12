package com.Flaground.backend.module.member.domain.repository;

import com.Flaground.backend.module.member.controller.dto.response.AvatarResponse;
import com.Flaground.backend.module.member.controller.dto.response.LoginLogResponse;
import com.Flaground.backend.module.member.controller.dto.response.MyProfileResponse;
import com.Flaground.backend.module.member.controller.dto.response.SearchMemberResponse;
import com.Flaground.backend.module.member.domain.Member;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> getDeactivateMembers();

    List<Member> getMembersByLoginIds(List<String> loginIdList);

    List<String> getAllBeforeSleepEmails();

    AvatarResponse getAvatar(String loginId);

    MyProfileResponse getMyProfile(Long memberId);

    List<LoginLogResponse> getAllLoginLogs();

    List<SearchMemberResponse> searchMemberByName(String name);
}
