package com.FlagHome.backend.domain.activity.controller;

import com.FlagHome.backend.domain.activity.dto.ActivityRequest;
import com.FlagHome.backend.domain.activity.entity.Activity;
import com.FlagHome.backend.domain.activity.mapper.ActivityMapper;
import com.FlagHome.backend.domain.activity.repository.ActivityRepository;
import com.FlagHome.backend.domain.activity.service.ActivityService;
import com.FlagHome.backend.global.utility.SecurityUtils;
import com.FlagHome.backend.global.utility.UriCreator;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Tag(name = "activity", description = "활동 API")
@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class ActivityController {
//    private static final String DEFAULT_URL = "/api/activity";
//    private final ActivityService activityService;
//    private final ActivityRepository activityRepository; // 임시
//    private final ActivityMapper mapper;
//
//    @Tag(name = "activity")
//    @PostMapping
//    public ResponseEntity<Void> createActivity(@RequestBody ActivityRequest activityRequest) {
//        Activity activity = activityService
//                .create(mapper.createDTOtoEntity(SecurityUtils.getMemberId(), activityRequest), activityRepository);
//        URI location = UriCreator.createUri(DEFAULT_URL, activity.getId());
//        return ResponseEntity.created(location).build();
//    }
}
