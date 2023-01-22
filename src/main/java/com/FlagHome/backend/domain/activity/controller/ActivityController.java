package com.FlagHome.backend.domain.activity.controller;

import com.FlagHome.backend.domain.HttpResponse;
import com.FlagHome.backend.domain.activity.dto.ActivityResponse;
import com.FlagHome.backend.domain.activity.dto.ChangeLeaderRequest;
import com.FlagHome.backend.domain.activity.dto.CloseRecruitRequest;
import com.FlagHome.backend.domain.activity.dto.ActivityRequest;
import com.FlagHome.backend.domain.activity.mapper.ActivityMapper;
import com.FlagHome.backend.domain.activity.service.ActivityService;
import com.FlagHome.backend.global.utility.SecurityUtils;
import com.FlagHome.backend.global.utility.UriCreator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.CREATED;

@Tag(name = "activity", description = "활동 API")
@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {
    private static final String DEFAULT_URL = "/api/activities";
    private final ActivityService activityService;
    private final ActivityMapper activityMapper;

    @Tag(name = "activity")
    @Operation(summary = "활동 상세보기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "활동 정보 가져오기 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 활동입니다.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<HttpResponse> getActivity(@PathVariable("id") long activityId) {
        HttpResponse response = HttpResponse.ok(activityService.getActivity(activityId), OK, "활동 정보 가져오기 성공");
        return ResponseEntity.ok(response);
    }

    @Tag(name = "activity")
    @Operation(summary = "모든 활동 가져오기", description = "현재 일부만 구현됨. 추후 변경될 예정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모든 활동 가져오기 성공"),
    })
    @GetMapping
    public ResponseEntity<HttpResponse> getAllActivities() {
        HttpResponse response = HttpResponse.ok(activityService.getAllActivities(), OK, "모든 활동 정보 가져오기 성공");
        return ResponseEntity.ok(response);
    }

    @Tag(name = "activity")
    @Operation(summary = "활동 신청여부 확인하기", description = "한 멤버가 한 활동에 한번만 신청할 수 있다, 덜 구현")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "활동 신청여부 확인 성공"),
            @ApiResponse(responseCode = "500", description = "서버 에러입니다. 관리자에게 문의해주세요.")
    })
    @PostMapping("/{id}/check")
    public ResponseEntity<HttpResponse> checkApply(@PathVariable("id") long activityId) {
        boolean check = activityService.checkApply(SecurityUtils.getMemberId(), activityId);
        HttpResponse response = HttpResponse.ok(check, OK, "활동 신청여부 확인 성공");
        return ResponseEntity.ok(response);
    }

    @Tag(name = "activity")
    @Operation(summary = "활동 신청하기", description = "한 멤버당 한 활동에 한번만 신청할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "활동 신청 성공"),
            @ApiResponse(responseCode = "401", description = "일반 유저가 신청하려고한 경우"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 활동입니다."),
            @ApiResponse(responseCode = "409", description = "이미 신청한 활동입니다.")
    })
    @PostMapping("/{id}/apply")
    public ResponseEntity<HttpResponse> applyActivity(@PathVariable("id") long activityId) {
        activityService.applyActivity(SecurityUtils.getMemberId(), activityId);
        HttpResponse response = HttpResponse.ok(null, CREATED, "활동 신청 성공");
        return ResponseEntity.ok(response);
    }

    @Tag(name = "activity")
    @Operation(summary = "활동 만들기", description = "활동 만들기는 동아리원만 가능하다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "활동 만들기 성공"),
            @ApiResponse(responseCode = "401", description = "일반 유저가 만들려고한 경우")
    })
    @PostMapping
    public ResponseEntity<HttpResponse> createActivity(@RequestBody ActivityRequest activityRequest) {
        long activityId = activityService.create(activityMapper.dtoToEntity(SecurityUtils.getMemberId(), activityRequest));
        URI location = UriCreator.createUri(DEFAULT_URL, activityId);
        HttpResponse response = HttpResponse.ok(location, CREATED, "활동 만들기 성공");
        return ResponseEntity.ok(response);
    }

    @Tag(name = "activity")
    @Operation(summary = "프로젝트 내용 수정하기", description = "활동장만 수정할 수 있다.\n" +
                                                               "프로젝트는 책 사용여부와 책 이름을 적지 않아도 된다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로젝트 내용 수정 성공"),
            @ApiResponse(responseCode = "401", description = "활동장이 아닙니다."),
    })
    @PutMapping("/project/{id}")
    public ResponseEntity<HttpResponse> updateProject(@PathVariable("id") long activityId,
                                              @RequestBody ActivityRequest activityRequest) {
        activityService.updateProject(SecurityUtils.getMemberId(), activityId, activityRequest);
        HttpResponse response = HttpResponse.ok(null, OK, "프로젝트 수정 성공");
        return ResponseEntity.ok(response);
    }

    @Tag(name = "activity")
    @Operation(summary = "멘토링 내용 수정하기", description = "활동장만 수정할 수 있다.\n" +
                                                             "멘토링은 깃허브 주소를 적지 않아도 된다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "멘토링 내용 수정 성공"),
            @ApiResponse(responseCode = "401", description = "활동장이 아닙니다."),
    })
    @PutMapping("/mentoring/{id}")
    public ResponseEntity<HttpResponse> updateMentoring(@PathVariable("id") long activityId,
                                                @RequestBody ActivityRequest activityRequest) {
        activityService.updateMentoring(SecurityUtils.getMemberId(), activityId, activityRequest);
        HttpResponse response = HttpResponse.ok(null, OK, "멘토링 수정 성공");
        return ResponseEntity.ok(response);
    }

    @Tag(name = "activity")
    @Operation(summary = "스터디 내용 수정하기", description = "활동장만 수정할 수 있다.\n" +
                                                              "스터디는 깃허브 주소를 적지 않아도 된다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "스터디 내용 수정 성공"),
            @ApiResponse(responseCode = "401", description = "활동장이 아닙니다."),
    })
    @PutMapping("/study/{id}")
    public ResponseEntity<HttpResponse> updateStudy(@PathVariable("id") long activityId,
                                            @RequestBody ActivityRequest activityRequest) {
        activityService.updateStudy(SecurityUtils.getMemberId(), activityId, activityRequest);
        HttpResponse response = HttpResponse.ok(null, OK, "스터디 수정 성공");
        return ResponseEntity.ok(response);
    }

    @Tag(name = "activity")
    @Operation(summary = "활동장 권한 넘기기", description = "새로운 활동장의 아이디를 받아 권한을 넘긴다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "활동장 권한 넘기기 성공"),
            @ApiResponse(responseCode = "401", description = "활동장이 아닙니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자입니다.")
    })
    @PatchMapping("/{id}/leader")
    public ResponseEntity<HttpResponse> updateLeader(@PathVariable("id") long activityId,
                                             @RequestBody ChangeLeaderRequest changeLeaderRequest) {
        activityService.changeLeader(SecurityUtils.getMemberId(), activityId, changeLeaderRequest.getLoginId());
        HttpResponse response = HttpResponse.ok(null, OK, "활동장 권한 넘기기 성공");
        return ResponseEntity.ok(response);
    }

    @Tag(name = "activity")
    @Operation(summary = "활동 모집 마감하기", description = "활동 모집 마감 시 활동장이 같이 활동할 멤버를 정한다. 덜 구현")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모집 마감 및 유저활동 등록 성공"),
            @ApiResponse(responseCode = "401", description = "활동장이 아닙니다.")
    })
    @PostMapping("/{id}/close")
    public ResponseEntity<HttpResponse> closeRecruitment(@PathVariable("id") long activityId,
                                                 @RequestBody CloseRecruitRequest closeRecruitRequest) {
        activityService.closeRecruitment(SecurityUtils.getMemberId(), activityId, closeRecruitRequest.getMemberList());
        HttpResponse response = HttpResponse.ok(null, OK, "모집 마감 성공");
        return ResponseEntity.ok(response);
    }

    @Tag(name = "activity")
    @Operation(summary = "활동 삭제하기", description = "활동장만 모집 중인 활동을 삭제할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "활동 삭제하기 성공"),
            @ApiResponse(responseCode = "401", description = "활동장이 아닙니다.")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<HttpResponse> deleteActivity(@PathVariable("id") long activityId) {
        activityService.delete(SecurityUtils.getMemberId(), activityId);
        HttpResponse response = HttpResponse.ok(null, OK, "활동 삭제 성공");
        return ResponseEntity.ok(response);
    }

    @Tag(name = "activity")
    @Operation(summary = "활동 신청 취소하기", description = "신청한 활동 취소하기, 덜구현")
    @DeleteMapping("/{id}/apply")
    public ResponseEntity<HttpResponse> cancleApply(@PathVariable("id") long activityId) {

        HttpResponse response = HttpResponse.ok(null, OK, "");
        return ResponseEntity.ok(response);
    }
}
