package com.FlagHome.backend.domain.activity.controller;

import com.FlagHome.backend.domain.activity.service.ActivityService;
import io.swagger.annotations.ApiModelProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityService activityService;

    @DeleteMapping("/{activity_Id}")
    @ApiModelProperty(value = "활동 삭제")
    public ResponseEntity<Void> deleteActivity(@PathVariable(name = "activity_Id") long activityId) {
        activityService.deleteActivity(activityId);
        return ResponseEntity.noContent().build();
    }
}
