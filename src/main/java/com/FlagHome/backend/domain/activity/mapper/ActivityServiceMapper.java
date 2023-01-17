package com.FlagHome.backend.domain.activity.mapper;

import com.FlagHome.backend.domain.activity.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActivityServiceMapper {
    // 책임 : 맞는 서비스를 매핑해준다.
    private final ActivityRepository activityRepository;
}
