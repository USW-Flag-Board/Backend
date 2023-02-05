package com.FlagHome.backend.domain.activity.controller;

import com.FlagHome.backend.domain.common.ApplicationResponse;
import com.FlagHome.backend.domain.activity.activityapply.dto.ActivityApplyResponse;
import com.FlagHome.backend.domain.activity.dto.ActivityRequest;
import com.FlagHome.backend.domain.activity.dto.ChangeLeaderRequest;
import com.FlagHome.backend.domain.activity.dto.CloseRecruitRequest;
import com.FlagHome.backend.domain.activity.entity.Activity;
import com.FlagHome.backend.domain.activity.memberactivity.dto.ParticipantResponse;
import com.FlagHome.backend.domain.activity.service.ActivityService;
import com.FlagHome.backend.global.utility.SecurityUtils;
import com.FlagHome.backend.global.utility.UriCreator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "activity", description = "활동 API")
@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {
    private static final String DEFAULT_URL = "/api/activities";
    private final ActivityService activityService;

    @Tag(name = "activity")
    @Operation(summary = "활동 상세보기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "활동 상세보기가 정상적으로 처리되었습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 활동입니다.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponse> getActivity(@PathVariable("id") long activityId) {
        return ResponseEntity.ok(ApplicationResponse.of(activityService.getActivity(activityId), OK, "활동 데이터를 가져왔습니다.."));
    }

    @Tag(name = "activity")
    @Operation(summary = "모든 활동 가져오기", description = "동아리 소개에 사용될 API\n" +
                                                          "연도 별로 활동 종류에 따라서 고유번호, 이름, 상태, 시즌을 리턴한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모든 활동들을 가져왔습니다."),
    })
    @GetMapping
    public ResponseEntity<ApplicationResponse> getAllActivities() {
        return ResponseEntity.ok(ApplicationResponse.of(activityService.getAllActivities(), OK, "모든 활동들을 가져왔습니다."));
    }

    @Tag(name = "activity")
    @Operation(summary = "모든 신청리스트 가져오기", description = "[토큰필요] 활동장 전용 기능, 신청받은 리스트를 가져온다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모든 신청리스트를 성공적으로 가져왔습니다."),
            @ApiResponse(responseCode = "401", description = "활동장이 아닙니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 활동입니다.")
    })
    @GetMapping("/{id}/apply")
    public ResponseEntity<ApplicationResponse> getAllActivityApplies(@PathVariable("id") long activityId) {
        List<ActivityApplyResponse> response = activityService.getAllActivityApplies(SecurityUtils.getMemberId(), activityId);
        return ResponseEntity.ok(ApplicationResponse.of(response, OK, "모든 신청을 가져왔습니다."));
    }

    @Tag(name = "activity")
    @Operation(summary = "활동원 리스트 가져오기", description = "[토큰필요] 활동장 전용기능" +
            "\n 활동원 관리용 API, 활동원들 리스트를 가져온다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "활동원들 정보를 가져왔습니다."),
            @ApiResponse(responseCode = "401", description = "활동장이 아닙니다.")
    })
    @GetMapping("/{id}/participant")
    public ResponseEntity<ApplicationResponse> getAllParticipants(@PathVariable("id") long activityId) {
        List<ParticipantResponse> response = activityService.getAllParticipants(SecurityUtils.getMemberId(), activityId);
        return ResponseEntity.ok(ApplicationResponse.of(response, OK, "활동원들 정보를 가져왔습니다."));
    }

    @Tag(name = "activity")
    @Operation(summary = "활동 신청여부 확인하기", description = "[토큰필요] 한 멤버가 한 활동에 한번만 신청할 수 있다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "신청여부 조회에 성공하였습니다."),
            @ApiResponse(responseCode = "500", description = "서버 에러입니다. 관리자에게 문의해주세요.")
    })
    @PostMapping("/{id}/check")
    public ResponseEntity<ApplicationResponse> checkApply(@PathVariable("id") long activityId) {
        boolean check = activityService.checkApply(SecurityUtils.getMemberId(), activityId);
        return ResponseEntity.ok(ApplicationResponse.of(check, OK, "신청여부 조회하였습니다."));
    }

    @Tag(name = "activity")
    @Operation(summary = "활동 신청하기", description = "[토큰필요] 한 멤버당 한 활동에 한번만 신청할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "신청에 성공하였습니다."),
            @ApiResponse(responseCode = "401", description = "일반 유저가 신청하려고한 경우"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 활동입니다."),
            @ApiResponse(responseCode = "409", description = "이미 신청한 활동입니다.")
    })
    @PostMapping("/{id}/apply")
    public ResponseEntity<ApplicationResponse> applyActivity(@PathVariable("id") long activityId) {
        activityService.applyActivity(SecurityUtils.getMemberId(), activityId);
        return ResponseEntity.ok(ApplicationResponse.of(null, CREATED, "신청에 성공했습니다."));
    }

    @Tag(name = "activity")
    @Operation(summary = "활동 만들기", description = "[토큰필요] 활동 만들기는 동아리원만 가능하다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "스터디 내용이 성공적으로 수정되었습니다."),
            @ApiResponse(responseCode = "401", description = "일반 유저가 만들려고한 경우")
    })
    @PostMapping
    public ResponseEntity<ApplicationResponse> createActivity(@RequestBody ActivityRequest activityRequest) {
        Activity activity = activityService.create(SecurityUtils.getMemberId(), activityRequest);
        URI uri = UriCreator.createUri(DEFAULT_URL, activity.getId());
        return ResponseEntity.ok(ApplicationResponse.of(uri, CREATED, "활동을 만들었습니다."));
    }

//    @Tag(name = "activity")
//    @Operation(summary = "활동 전용 게시판 요청하기", description = "활동장 전용 기능. 게시판 생성을 관리자에게 요청한다.")
//    @ApiResponses({
//            @ApiResponse(responseCode = "201", description = "활동 게시판 요청 성공"),
//            @ApiResponse(responseCode = "401", description = "활동장이 아닙니다.")
//    })
//    @PostMapping("/{id}/board")
//    public ResponseEntity<HttpResponse> requestBoard(@PathVariable("id") long activityId) {
//
//        HttpResponse response = HttpResponse.ok(null, CREATED, "활동 게시판 요청 성공");
//        return ResponseEntity.ok(response);
//    }

    @Tag(name = "activity")
    @Operation(summary = "프로젝트 내용 수정하기", description = "[토큰필요] 활동장만 수정할 수 있다.\n" +
                                                               "프로젝트는 책 사용여부와 책 이름을 적지 않아도 된다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로젝트 내용이 성공적으로 수정되었습니다."),
            @ApiResponse(responseCode = "401", description = "활동장이 아닙니다."),
    })
    @PutMapping("/project/{id}")
    public ResponseEntity<ApplicationResponse> updateProject(@PathVariable("id") long activityId,
                                                             @RequestBody ActivityRequest activityRequest) {
        activityService.updateProject(SecurityUtils.getMemberId(), activityId, activityRequest);
        URI uri = UriCreator.createUri(DEFAULT_URL, activityId);
        return ResponseEntity.ok(ApplicationResponse.of(uri, OK, "프로젝트 내용이 성공적으로 수정되었습니다."));
    }

    @Tag(name = "activity")
    @Operation(summary = "멘토링 내용 수정하기", description = "[토큰필요] 활동장만 수정할 수 있다.\n" +
                                                             "멘토링은 깃허브 주소를 적지 않아도 된다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "멘토링 내용이 성공적으로 수정되었습니다."),
            @ApiResponse(responseCode = "401", description = "활동장이 아닙니다."),
    })
    @PutMapping("/mentoring/{id}")
    public ResponseEntity<ApplicationResponse> updateMentoring(@PathVariable("id") long activityId,
                                                               @RequestBody ActivityRequest activityRequest) {
        activityService.updateMentoring(SecurityUtils.getMemberId(), activityId, activityRequest);
        URI uri = UriCreator.createUri(DEFAULT_URL, activityId);
        return ResponseEntity.ok(ApplicationResponse.of(uri, OK, "멘토링 내용이 성공적으로 수정되었습니다."));
    }

    @Tag(name = "activity")
    @Operation(summary = "스터디 내용 수정하기", description = "[토큰필요] 활동장만 수정할 수 있다.\n" +
                                                              "스터디는 깃허브 주소를 적지 않아도 된다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "스터디 내용이 성공적으로 수정되었습니다."),
            @ApiResponse(responseCode = "401", description = "활동장이 아닙니다."),
    })
    @PutMapping("/study/{id}")
    public ResponseEntity<ApplicationResponse> updateStudy(@PathVariable("id") long activityId,
                                                           @RequestBody ActivityRequest activityRequest) {
        activityService.updateStudy(SecurityUtils.getMemberId(), activityId, activityRequest);
        URI uri = UriCreator.createUri(DEFAULT_URL, activityId);
        return ResponseEntity.ok(ApplicationResponse.of(uri, OK, "스터디 내용이 성공적으로 수정되었습니다."));
    }

    @Tag(name = "activity")
    @Operation(summary = "활동장 권한 넘기기", description = "[토큰필요] 새로운 활동장의 아이디를 받아 권한을 넘긴다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "권한을 성공적으로 넘겼습니다."),
            @ApiResponse(responseCode = "401", description = "활동장이 아닙니다."),
            @ApiResponse(responseCode = "404", description = "이 활동의 멤버가 아닙니다.")
    })
    @PatchMapping("/{id}/leader")
    public ResponseEntity<ApplicationResponse> updateLeader(@PathVariable("id") long activityId,
                                                            @RequestBody ChangeLeaderRequest changeLeaderRequest) {
        activityService.changeLeader(SecurityUtils.getMemberId(), activityId, changeLeaderRequest.getLoginId());
        URI uri = UriCreator.createUri(DEFAULT_URL, activityId);
        return ResponseEntity.ok(ApplicationResponse.of(uri, OK, "권한을 성공적으로 넘겼습니다."));
    }

    @Tag(name = "activity")
    @Operation(summary = "활동 모집 마감하기", description = "[토큰필요] 활동 모집 마감 시 활동장이 같이 활동할 멤버를 정한다. 덜 구현")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모집이 마감되었습니다."),
            @ApiResponse(responseCode = "401", description = "활동장이 아닙니다.")
    })
    @PatchMapping("/{id}/close")
    public ResponseEntity<ApplicationResponse> closeRecruitment(@PathVariable("id") long activityId,
                                                                @RequestBody CloseRecruitRequest closeRecruitRequest) {
        activityService.closeRecruitment(SecurityUtils.getMemberId(), activityId, closeRecruitRequest.getLoginIdList());
        URI uri = UriCreator.createUri(DEFAULT_URL, activityId);
        return ResponseEntity.ok(ApplicationResponse.of(uri, OK, "모집이 마감되었습니다."));
    }

    @Tag(name = "activity")
    @Operation(summary = "활동 모집 다시 열기", description = "[토큰필요] 추가적인 활동원을 받기 위해서 상태를 모집중으로 변경한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "다시 모집을 시작합니다."),
            @ApiResponse(responseCode = "401", description = "활동장이 아닙니다.")
    })
    @PostMapping("/{id}/reopen")
    public ResponseEntity<ApplicationResponse> reopenRecruitment(@PathVariable("id") long activityId) {
        activityService.reopenRecruitment(SecurityUtils.getMemberId(), activityId);
        URI uri = UriCreator.createUri(DEFAULT_URL, activityId);
        return ResponseEntity.ok(ApplicationResponse.of(uri, OK, "다시 모집을 시작합니다."));
    }

    @Tag(name = "activity")
    @Operation(summary = "활동 마무리하기", description = "[토큰필요] 소기의 목적을 달성하고 활동을 끝낸다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "활동이 종료되었습니다."),
            @ApiResponse(responseCode = "401", description = "활동장이 아닙니다.")
    })
    @PatchMapping("/{id}/finish")
    public ResponseEntity<ApplicationResponse> finishActivity(@PathVariable("id") long activityId) {
        activityService.finishActivity(SecurityUtils.getMemberId(), activityId);
        URI uri = UriCreator.createUri(DEFAULT_URL, activityId);
        return ResponseEntity.ok(ApplicationResponse.of(uri, OK, "활동이 종료되었습니다."));
    }

    @Tag(name = "activity")
    @Operation(summary = "활동 삭제하기", description = "[토큰필요] 활동장만 모집 중인 활동을 삭제할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "활동이 삭제되었습니다."),
            @ApiResponse(responseCode = "401", description = "활동장이 아닙니다.")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<ApplicationResponse> deleteActivity(@PathVariable("id") long activityId) {
        activityService.delete(SecurityUtils.getMemberId(), activityId);
        return ResponseEntity.ok(ApplicationResponse.of(null, OK, "활동이 삭제되었습니다."));
    }

    @Tag(name = "activity")
    @Operation(summary = "활동 신청 취소하기", description = "[토큰필요] 신청한 활동 취소하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "신청이 취소되었습니다."),
            @ApiResponse(responseCode = "404", description = "존재하는 신청 내역이 없습니다.")
    })
    @DeleteMapping("/{id}/apply")
    public ResponseEntity<ApplicationResponse> cancelApply(@PathVariable("id") long activityId) {
        activityService.cancelApply(SecurityUtils.getMemberId(), activityId);
        return ResponseEntity.ok(ApplicationResponse.of(null, OK, "신청이 취소되었습니다."));
    }
}
