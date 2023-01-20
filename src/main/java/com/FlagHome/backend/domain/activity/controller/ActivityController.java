package com.FlagHome.backend.domain.activity.controller;

import com.FlagHome.backend.domain.activity.dto.*;
import com.FlagHome.backend.domain.activity.mapper.ActivityMapper;
import com.FlagHome.backend.domain.activity.service.ActivityService;
import com.FlagHome.backend.global.utility.SecurityUtils;
import com.FlagHome.backend.global.utility.UriCreator;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "activity", description = "활동 API")
@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {
    private static final String DEFAULT_URL = "/api/activities";
    private final ActivityService activityService;
    private final ActivityMapper activityMapper;

    @Tag(name = "activity")
    @GetMapping("/{id}")
    public ResponseEntity<ActivityResponse> getActivity(@PathVariable("id") long activityId) {
        return ResponseEntity.ok(activityService.getActivity(activityId));
    }

    @Tag(name = "activity")
    @GetMapping
    public ResponseEntity<List<ActivityResponse>> getAllActivities() {
        return ResponseEntity.ok(activityService.getAllActivities());
    }

    @Tag(name = "activity")
    @PostMapping("/apply/{id}")
    public ResponseEntity<Void> checkApply(@PathVariable("id") long activityId) {
        boolean check = activityService.checkApply(SecurityUtils.getMemberId(), activityId);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "activity")
    @PostMapping("/{id}/apply")
    public ResponseEntity<Void> applyActivity(@PathVariable("id") long activityId) {
        activityService.applyActivity(SecurityUtils.getMemberId(), activityId);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "activity")
    @PostMapping
    public ResponseEntity<Void> createActivity(@RequestBody CreateActivityRequest createActivityRequest) {
        long activityId = activityService.create(activityMapper.dtoToEntity(SecurityUtils.getMemberId(), createActivityRequest));
        URI location = UriCreator.createUri(DEFAULT_URL, activityId);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "activity")
    @PutMapping("/project")
    public ResponseEntity<Void> updateProject(@RequestBody UpdateActivityRequest updateActivityRequest) {
        activityService.updateProject(SecurityUtils.getMemberId(), updateActivityRequest);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "activity")
    @PutMapping("/mentoring")
    public ResponseEntity<Void> updateMentoring(@RequestBody UpdateActivityRequest updateActivityRequest) {
        activityService.updateMentoring(SecurityUtils.getMemberId(), updateActivityRequest);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "activity")
    @PutMapping("/study")
    public ResponseEntity<Void> updateStudy(@RequestBody UpdateActivityRequest updateActivityRequest) {
        activityService.updateStudy(SecurityUtils.getMemberId(), updateActivityRequest);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "activity")
    @PatchMapping("/leader")
    public ResponseEntity<Void> updateLeader(@RequestBody ChangeLeaderRequest changeLeaderRequest) {
        activityService.changeLeader(SecurityUtils.getMemberId(), changeLeaderRequest);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "activity")
    @PatchMapping("/close")
    public ResponseEntity<Void> closeRecruitment(@RequestBody CloseActivityRequest closeActivityRequest) {
        activityService.closeRecruitment(SecurityUtils.getMemberId(), closeActivityRequest);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "activity")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable("id") long activityId) {
        activityService.delete(SecurityUtils.getMemberId(), activityId);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "activity")
    @DeleteMapping("/{id}/apply")
    public ResponseEntity<Void> cancleApply(@PathVariable("id") long activityId) {

        return ResponseEntity.ok().build();
    }
}