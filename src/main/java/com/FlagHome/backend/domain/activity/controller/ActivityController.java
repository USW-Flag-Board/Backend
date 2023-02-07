package com.FlagHome.backend.domain.activity.controller;

import com.FlagHome.backend.domain.activity.activityapply.dto.ActivityApplyResponse;
import com.FlagHome.backend.domain.activity.dto.*;
import com.FlagHome.backend.domain.activity.memberactivity.dto.ParticipantResponse;
import com.FlagHome.backend.domain.activity.service.ActivityService;
import com.FlagHome.backend.domain.common.ApplicationResponse;
import com.FlagHome.backend.global.utility.SecurityUtils;
import com.FlagHome.backend.global.utility.UriCreator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
    @ResponseStatus(OK)
    @GetMapping("/{id}")
    public ApplicationResponse<ActivityResponse> getActivity(@PathVariable("id") long activityId) {
        ActivityResponse response = activityService.getActivity(activityId);
        return new ApplicationResponse(response);
    }

    @Tag(name = "activity")
    @Operation(summary = "모든 활동 가져오기", description = "동아리 소개에 사용될 API\n\n" +
                                                          "연도 별로 활동 종류에 따라서 고유번호, 이름, 상태, 시즌을 리턴한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모든 활동들을 가져왔습니다."),
    })
    @ResponseStatus(OK)
    @GetMapping
    public ApplicationResponse<GetAllActivitiesResponse> getAllActivities() {
        GetAllActivitiesResponse response = activityService.getAllActivities();
        return new ApplicationResponse(response);
    }

    @Tag(name = "activity")
    @Operation(summary = "모든 신청리스트 가져오기", description = "[토큰필요] 활동장 전용 기능, 신청받은 리스트를 가져온다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모든 신청리스트를 성공적으로 가져왔습니다."),
            @ApiResponse(responseCode = "401", description = "활동장이 아닙니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 활동입니다.")
    })
    @ResponseStatus(OK)
    @GetMapping("/{id}/apply")
    public ApplicationResponse<List<ActivityApplyResponse>> getAllActivityApplies(@PathVariable("id") long activityId) {
        List<ActivityApplyResponse> response = activityService.getAllActivityApplies(SecurityUtils.getMemberId(), activityId);
        return new ApplicationResponse(response);
    }

    @Tag(name = "activity")
    @Operation(summary = "활동원 리스트 가져오기", description = "[토큰필요] 활동장 전용기능" +
            "\n\n활동원 관리용 API, 활동원들 리스트를 가져온다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "활동원들 정보를 가져왔습니다."),
            @ApiResponse(responseCode = "401", description = "활동장이 아닙니다.")
    })
    @ResponseStatus(OK)
    @GetMapping("/{id}/participant")
    public ApplicationResponse<List<ParticipantResponse>> getAllParticipants(@PathVariable("id") long activityId) {
        List<ParticipantResponse> response = activityService.getAllParticipants(SecurityUtils.getMemberId(), activityId);
        return new ApplicationResponse(response);
    }

    @Tag(name = "activity")
    @Operation(summary = "활동 신청여부 확인하기", description = "[토큰필요] 한 멤버가 한 활동에 한번만 신청할 수 있다.\n\n" +
            "False : 미신청, True : 신청")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "신청여부 조회에 성공하였습니다."),
            @ApiResponse(responseCode = "500", description = "서버 에러입니다. 관리자에게 문의해주세요.")
    })
    @ResponseStatus(OK)
    @PostMapping("/{id}/check")
    public ApplicationResponse<Boolean> checkApply(@PathVariable("id") long activityId) {
        Boolean check = activityService.checkApply(SecurityUtils.getMemberId(), activityId);
        return new ApplicationResponse(check);
    }

    @Tag(name = "activity")
    @Operation(summary = "활동 신청하기", description = "[토큰필요] 한 멤버당 한 활동에 한번만 신청할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "신청에 성공하였습니다."),
            @ApiResponse(responseCode = "401", description = "일반 유저가 신청하려고한 경우"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 활동입니다."),
            @ApiResponse(responseCode = "409", description = "이미 신청한 활동입니다.")
    })
    @ResponseStatus(CREATED)
    @PostMapping("/{id}/apply")
    public ApplicationResponse applyActivity(@PathVariable("id") long activityId) {
        activityService.applyActivity(SecurityUtils.getMemberId(), activityId);
        return new ApplicationResponse();
    }

    @Tag(name = "activity")
    @Operation(summary = "활동 만들기", description = "[토큰필요] 활동 만들기는 동아리원만 가능하다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "스터디 내용이 성공적으로 수정되었습니다."),
            @ApiResponse(responseCode = "401", description = "일반 유저가 만들려고한 경우")
    })
    @ResponseStatus(CREATED)
    @PostMapping
    public ApplicationResponse<URI> createActivity(@RequestBody ActivityRequest activityRequest) {
        long id = activityService.create(SecurityUtils.getMemberId(), activityRequest).getId();
        URI uri = UriCreator.createUri(DEFAULT_URL, id);
        return new ApplicationResponse(uri);
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
    @Operation(summary = "프로젝트 내용 수정하기", description = "[토큰필요] 활동장만 수정할 수 있다.\n\n" +
                                                               "프로젝트는 책 사용여부와 책 이름을 적지 않아도 된다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로젝트 내용이 성공적으로 수정되었습니다."),
            @ApiResponse(responseCode = "401", description = "활동장이 아닙니다."),
    })
    @ResponseStatus(OK)
    @PutMapping("/project/{id}")
    public ApplicationResponse<URI> updateProject(@PathVariable("id") long activityId,
                                                  @RequestBody ActivityRequest activityRequest) {
        activityService.updateProject(SecurityUtils.getMemberId(), activityId, activityRequest);
        URI uri = UriCreator.createUri(DEFAULT_URL, activityId);
        return new ApplicationResponse(uri);
    }

    @Tag(name = "activity")
    @Operation(summary = "멘토링 내용 수정하기", description = "[토큰필요] 활동장만 수정할 수 있다.\n\n" +
                                                             "멘토링은 깃허브 주소를 적지 않아도 된다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "멘토링 내용이 성공적으로 수정되었습니다."),
            @ApiResponse(responseCode = "401", description = "활동장이 아닙니다."),
    })
    @ResponseStatus(OK)
    @PutMapping("/mentoring/{id}")
    public ApplicationResponse<URI> updateMentoring(@PathVariable("id") long activityId,
                                                    @RequestBody ActivityRequest activityRequest) {
        activityService.updateMentoring(SecurityUtils.getMemberId(), activityId, activityRequest);
        URI uri = UriCreator.createUri(DEFAULT_URL, activityId);
        return new ApplicationResponse(uri);
    }

    @Tag(name = "activity")
    @Operation(summary = "스터디 내용 수정하기", description = "[토큰필요] 활동장만 수정할 수 있다.\n\n" +
                                                              "스터디는 깃허브 주소를 적지 않아도 된다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "스터디 내용이 성공적으로 수정되었습니다."),
            @ApiResponse(responseCode = "401", description = "활동장이 아닙니다."),
    })
    @ResponseStatus(OK)
    @PutMapping("/study/{id}")
    public ApplicationResponse<URI> updateStudy(@PathVariable("id") long activityId,
                                                @RequestBody ActivityRequest activityRequest) {
        activityService.updateStudy(SecurityUtils.getMemberId(), activityId, activityRequest);
        URI uri = UriCreator.createUri(DEFAULT_URL, activityId);
        return new ApplicationResponse(uri);
    }

    @Tag(name = "activity")
    @Operation(summary = "활동장 권한 넘기기", description = "[토큰필요] 새로운 활동장의 아이디를 받아 권한을 넘긴다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "권한을 성공적으로 넘겼습니다."),
            @ApiResponse(responseCode = "401", description = "활동장이 아닙니다."),
            @ApiResponse(responseCode = "404", description = "이 활동의 멤버가 아닙니다.")
    })
    @ResponseStatus(OK)
    @PatchMapping("/{id}/leader")
    public ApplicationResponse<URI> updateLeader(@PathVariable("id") long activityId,
                                                 @RequestBody ChangeLeaderRequest changeLeaderRequest) {
        activityService.changeLeader(SecurityUtils.getMemberId(), activityId, changeLeaderRequest.getLoginId());
        URI uri = UriCreator.createUri(DEFAULT_URL, activityId);
        return new ApplicationResponse(uri);
    }

    @Tag(name = "activity")
    @Operation(summary = "활동 모집 마감하기", description = "[토큰필요] 활동 모집 마감 시 활동장이 같이 활동할 멤버를 정한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모집이 마감되었습니다."),
            @ApiResponse(responseCode = "401", description = "활동장이 아닙니다.")
    })
    @PatchMapping("/{id}/close")
    public ApplicationResponse<URI> closeRecruitment(@PathVariable("id") long activityId,
                                                                @RequestBody CloseRecruitRequest closeRecruitRequest) {
        activityService.closeRecruitment(SecurityUtils.getMemberId(), activityId, closeRecruitRequest.getLoginIdList());
        URI uri = UriCreator.createUri(DEFAULT_URL, activityId);
        return new ApplicationResponse(uri);
    }

    @Tag(name = "activity")
    @Operation(summary = "활동 모집 다시 열기", description = "[토큰필요] 추가적인 활동원을 받기 위해서 상태를 모집중으로 변경한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "다시 모집을 시작합니다."),
            @ApiResponse(responseCode = "401", description = "활동장이 아닙니다.")
    })
    @ResponseStatus(OK)
    @PostMapping("/{id}/reopen")
    public ApplicationResponse<URI> reopenRecruitment(@PathVariable("id") long activityId) {
        activityService.reopenRecruitment(SecurityUtils.getMemberId(), activityId);
        URI uri = UriCreator.createUri(DEFAULT_URL, activityId);
        return new ApplicationResponse(uri);
    }

    @Tag(name = "activity")
    @Operation(summary = "활동 마무리하기", description = "[토큰필요] 소기의 목적을 달성하고 활동을 끝낸다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "활동이 종료되었습니다."),
            @ApiResponse(responseCode = "401", description = "활동장이 아닙니다.")
    })
    @ResponseStatus(OK)
    @PatchMapping("/{id}/finish")
    public ApplicationResponse<URI> finishActivity(@PathVariable("id") long activityId) {
        activityService.finishActivity(SecurityUtils.getMemberId(), activityId);
        URI uri = UriCreator.createUri(DEFAULT_URL, activityId);
        return new ApplicationResponse(uri);
    }

    @Tag(name = "activity")
    @Operation(summary = "활동 삭제하기", description = "[토큰필요] 활동장만 모집 중인 활동을 삭제할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "활동이 삭제되었습니다."),
            @ApiResponse(responseCode = "401", description = "활동장이 아닙니다.")
    })
    @ResponseStatus(OK)
    @DeleteMapping("{id}")
    public ApplicationResponse deleteActivity(@PathVariable("id") long activityId) {
        activityService.delete(SecurityUtils.getMemberId(), activityId);
        return new ApplicationResponse();
    }

    @Tag(name = "activity")
    @Operation(summary = "활동 신청 취소하기", description = "[토큰필요] 신청한 활동 취소하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "신청이 취소되었습니다."),
            @ApiResponse(responseCode = "404", description = "존재하는 신청 내역이 없습니다.")
    })
    @ResponseStatus(OK)
    @DeleteMapping("/{id}/apply")
    public ApplicationResponse cancelApply(@PathVariable("id") long activityId) {
        activityService.cancelApply(SecurityUtils.getMemberId(), activityId);
        return new ApplicationResponse();
    }
}
