package com.FlagHome.backend.domain.activity.mapper;

import com.FlagHome.backend.domain.activity.ActivityType;
import com.FlagHome.backend.domain.activity.dto.ActivityRequest;
import com.FlagHome.backend.domain.activity.entity.Activity;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class ActivityMapper {
    private final MemberRepository memberRepository;

    public Activity dtoToEntity(long memberId, ActivityRequest activityRequest) {
        Member member =  memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Activity activity = Arrays.stream(ActivityType.values())
                .filter(type -> StringUtils.equals(type.getType(), activityRequest.getActivityType().toString()))
                .findFirst()
                .map(type -> type.toEntity(activityRequest))
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_SUPPORT_ACTIVITY));

        activity.setLeader(member);
        return activity;
    }
}
