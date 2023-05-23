package com.Flaground.backend.module.activity.service;

import com.Flaground.backend.module.activity.domain.Activity;
import com.Flaground.backend.module.activity.controller.dto.response.ParticipantResponse;
import com.Flaground.backend.module.activity.controller.dto.response.ParticipateResponse;
import com.Flaground.backend.module.activity.domain.MemberActivity;
import com.Flaground.backend.module.activity.domain.repository.MemberActivityRepository;
import com.Flaground.backend.module.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberActivityService {
    private final MemberActivityRepository memberActivityRepository;

    @Transactional(readOnly = true)
    public List<ParticipateResponse> getAllActivitiesOfMember(String loginId) {
        return memberActivityRepository.getAllActivitiesOfMember(loginId);
    }

    @Transactional(readOnly = true)
    public List<ParticipantResponse> getAllParticipants(Long activityId) {
        return memberActivityRepository.getAllParticipantByActivityId(activityId);
    }

    public void registerMembers(Activity activity, List<Member> memberList) {
        List<MemberActivity> memberActivityList = convertToMemberActivity(activity, memberList);
        memberActivityRepository.saveAll(memberActivityList);
    }

    private List<MemberActivity> convertToMemberActivity(Activity activity, List<Member> memberList) {
        return memberList.stream()
                .map(member -> MemberActivity.of(member, activity))
                .collect(Collectors.toList());
    }
}
