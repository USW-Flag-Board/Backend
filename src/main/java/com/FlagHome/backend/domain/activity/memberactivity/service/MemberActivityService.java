package com.FlagHome.backend.domain.activity.memberactivity.service;

import com.FlagHome.backend.domain.activity.entity.Activity;
import com.FlagHome.backend.domain.activity.memberactivity.dto.ParticipantResponse;
import com.FlagHome.backend.domain.activity.memberactivity.dto.ParticipateResponse;
import com.FlagHome.backend.domain.activity.memberactivity.entity.MemberActivity;
import com.FlagHome.backend.domain.activity.memberactivity.repository.MemberActivityRepository;
import com.FlagHome.backend.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberActivityService {
    private final MemberActivityRepository memberActivityRepository;

    public Member findMemberOfActivity(long activityId, String loginId) {
        return memberActivityRepository.findMemberOfActivityByLoginId(activityId, loginId);
    }

    @Transactional
    public void registerMembers(Activity activity, List<Member> memberList) {
        List<MemberActivity> memberActivityList = memberList.stream()
                        .map(member -> MemberActivity.of(member, activity))
                        .collect(Collectors.toList());

        memberActivityRepository.saveAll(memberActivityList);
    }

    @Transactional(readOnly = true)
    public List<ParticipateResponse> getAllActivitiesOfMember(String loginId) {
        return memberActivityRepository.getAllActivitiesOfMember(loginId);
    }

    @Transactional(readOnly = true)
    public List<ParticipantResponse> getAllParticipants(long activityId) {
        return memberActivityRepository.getAllParticipantByActivityId(activityId);
    }

    @Transactional
    public void deleteAllByActivity(long activityId) {
        memberActivityRepository.deleteAllByActivityId(activityId);
    }
}
