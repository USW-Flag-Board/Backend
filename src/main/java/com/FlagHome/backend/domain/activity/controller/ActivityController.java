package com.FlagHome.backend.domain.activity.controller;


import com.FlagHome.backend.domain.activity.entity.Activity;
import com.FlagHome.backend.domain.activity.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityService activityService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable(name = "id") Activity activityId) {
        activityService.deleteActivity(activityId);
        return ResponseEntity.noContent().build();
    }
}
