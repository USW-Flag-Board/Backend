package com.FlagHome.backend.domain.activity.controller;

import com.FlagHome.backend.domain.activity.service.MentoringService;
import com.FlagHome.backend.domain.activity.service.ProjectService;
import com.FlagHome.backend.domain.activity.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class ActivityController {
    private final MentoringService mentoringService;
    private final ProjectService projectService;
    private final StudyService studyService;
/*
    @DeleteMapping("/{activity_Id}")
    @ApiModelProperty(value = "활동 삭제")
    public ResponseEntity<Void> deleteActivity(@PathVariable(name = "activity_Id") long activityId) {
        MentoringService.deleteActivity(activityId);
        return ResponseEntity.noContent().build();
    }*/
}
