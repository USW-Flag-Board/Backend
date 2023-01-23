package com.FlagHome.backend.domain.activity.memberactivity.service;

import com.FlagHome.backend.domain.activity.entity.Activity;
import com.FlagHome.backend.domain.activity.memberactivity.entity.MemberActivity;
import com.FlagHome.backend.domain.activity.memberactivity.repository.MemberActivityRepository;
import com.FlagHome.backend.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberActivityService {
    private final MemberActivityRepository memberActivityRepository;

    @Transactional
    public void registerMembers(Activity activity, List<Member> memberList) {
        List<MemberActivity> memberActivityList = memberList.stream()
                        .map(member -> MemberActivity.builder().member(member).activity(activity).build())
                        .collect(Collectors.toList());

        memberActivityRepository.saveAll(memberActivityList);
    }
}
