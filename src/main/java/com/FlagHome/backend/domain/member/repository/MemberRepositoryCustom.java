package com.FlagHome.backend.domain.member.repository;

import com.FlagHome.backend.domain.member.controller.dto.response.AvatarResponse;
import com.FlagHome.backend.domain.member.controller.dto.response.LoginLogResponse;
import com.FlagHome.backend.domain.member.controller.dto.response.MyProfileResponse;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.controller.dto.response.SearchMemberResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepositoryCustom {
    List<Member> getAllSleepMembers();

    List<Member> getMembersByLoginIds(List<String> loginIdList);

    List<String> getAllBeforeSleepEmails();

    AvatarResponse getAvatar(String loginId);

    MyProfileResponse getMyProfile(Long memberId);

    List<LoginLogResponse> getAllLoginLogs();

    List<SearchMemberResponse> searchMemberByName(String name);
}
