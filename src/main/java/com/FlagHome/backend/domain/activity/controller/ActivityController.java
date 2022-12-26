package com.FlagHome.backend.domain.activity.controller;

import com.FlagHome.backend.domain.activity.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityService activityService;
}
