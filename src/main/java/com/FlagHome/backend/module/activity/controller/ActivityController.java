package com.FlagHome.backend.module.activity.controller;

import com.FlagHome.backend.module.activity.controller.dto.response.ActivityApplyResponse;
import com.FlagHome.backend.module.activity.controller.dto.request.CloseRecruitRequest;
import com.FlagHome.backend.module.activity.controller.dto.request.CreateActivityRequest;
import com.FlagHome.backend.module.activity.controller.dto.request.UpdateActivityRequest;
import com.FlagHome.backend.module.activity.controller.dto.response.ActivityDetailResponse;
import com.FlagHome.backend.module.activity.controller.dto.response.ActivityResponse;
import com.FlagHome.backend.module.activity.controller.dto.response.GetAllActivitiesResponse;
import com.FlagHome.backend.module.activity.entity.Activity;
import com.FlagHome.backend.module.activity.mapper.ActivityMapper;
import com.FlagHome.backend.module.activity.controller.dto.response.ParticipantResponse;
import com.FlagHome.backend.module.activity.controller.dto.response.ParticipateResponse;
import com.FlagHome.backend.module.activity.service.ActivityService;
import com.FlagHome.backend.global.common.ApplicationResponse;
import com.FlagHome.backend.global.utility.SecurityUtils;
import com.FlagHome.backend.global.utility.UriCreator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "activity", description = "활동 API")
@RestController
@RequestMapping("/activities")
@RequiredArgsConstructor
public class ActivityController {
    private static final String DEFAULT_URL = "/activities";
    private final ActivityService activityService;
    private final ActivityMapper activityMapper;

    @Tag(name = "activity")
    @Operation(summary = "활동 상세보기", description = "선택한 활동 상세보기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "활동 정보 가져오기 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 활동입니다.")
    })
    @ResponseStatus(OK)
    @GetMapping("/{id}")
    public ApplicationResponse<ActivityDetailResponse> getActivity(@PathVariable Long id) {
        Activity activity = activityService.getActivity(id);
        return new ApplicationResponse<>(activityMapper.toDetailResponse(activity));
    }

    @Tag(name = "activity")
    @Operation(summary = "모든 활동 가져오기")
    @ApiResponse(responseCode = "200", description = "모든 활동들을 가져왔습니다.")
    @ResponseStatus(OK)
    @GetMapping
    public ApplicationResponse<GetAllActivitiesResponse> getAllActivities() {
        GetAllActivitiesResponse response = activityService.getAllActivities();
        return new ApplicationResponse<>(response);
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
    public ApplicationResponse<List<ActivityApplyResponse>> getAllApplies(@PathVariable Long id) {
        List<ActivityApplyResponse> response = activityService.getAllApplies(SecurityUtils.getMemberId(), id);
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
    public ApplicationResponse<List<ParticipantResponse>> getAllParticipants(@PathVariable Long id) {
        List<ParticipantResponse> response = activityService.getAllParticipants(SecurityUtils.getMemberId(), id);
        return new ApplicationResponse(response);
    }

    @Tag(name = "activity")
    @Operation(summary = "멤버 페이지 정보 가져오기 (활동)", description = "멤버 페이지 참가 활동 리스트 가져오기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "참가한 활동이 없다면 빈 리스트 리턴"),
            @ApiResponse(responseCode = "400", description = "탈퇴한 유저, 리다이렉트 해줄 것"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자입니다."),
    })
    @ResponseStatus(OK)
    @GetMapping("/{loginId}/profile")
    public ApplicationResponse<List<ParticipateResponse>> getMemberPageActivities(@PathVariable String loginId) {
        List<ParticipateResponse> response = activityService.getMemberPageActivities(loginId);
        return new ApplicationResponse<>(response);
    }

    @Tag(name = "activity")
    @Operation(summary = "모집 중인 활동 가져오기", description = "홈페이지 전용 기능, 모집 중인 활동을 가져온다.")
    @ResponseStatus(OK)
    @GetMapping("/recruit")
    public ApplicationResponse<List<ActivityResponse>> getRecruitActivities() {
        List<ActivityResponse> response = activityService.getRecruitActivities();
        return new ApplicationResponse<>(response);
    }

    @Tag(name = "activity")
    @Operation(summary = "활동 만들기", description = "[토큰필요] 활동 만들기는 동아리원만 가능하다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "만들기 성공"),
            @ApiResponse(responseCode = "401", description = "일반 유저가 만들려고한 경우")
    })
    @ResponseStatus(CREATED)
    @PostMapping
    public ApplicationResponse<URI> createActivity(@RequestBody @Valid CreateActivityRequest createActivityRequest) {
        Activity activity = activityMapper.mapFrom(createActivityRequest);
        Long id = activityService.create(SecurityUtils.getMemberId(), activity).getId();
        URI uri = UriCreator.createURI(DEFAULT_URL, id);
        return new ApplicationResponse<>(uri);
    }

    @Tag(name = "activity")
    @Operation(summary = "활동 신청여부 확인하기", description = "[토큰필요] 한 멤버가 한 활동에 한번만 신청할 수 있다.\n\n" +
            "False : 미신청, True : 신청")
    @ApiResponse(responseCode = "200", description = "신청여부 조회에 성공하였습니다.")
    @ResponseStatus(OK)
    @PostMapping("/{id}/check")
    public ApplicationResponse<Boolean> checkApply(@PathVariable Long id) {
        Boolean check = activityService.checkApply(SecurityUtils.getMemberId(), id);
        return new ApplicationResponse<>(check);
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
    public ApplicationResponse applyActivity(@PathVariable Long id) {
        activityService.applyActivity(SecurityUtils.getMemberId(), id);
        return new ApplicationResponse<>();
    }

    @Tag(name = "activity")
    @Operation(summary = "활동 수정하기", description = "[토큰필요] 활동장만 수정 가능")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정에 성공했습니다."),
            @ApiResponse(responseCode = "401", description = "활동장이 아닙니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 활동입니다.")

    })
    @ResponseStatus(OK)
    @PutMapping("/{id}")
    public ApplicationResponse updateActivity(@PathVariable Long id,
                                              @RequestBody @Valid UpdateActivityRequest updateActivityRequest) {
        activityService.update(SecurityUtils.getMemberId(), id, activityMapper.mapFrom(updateActivityRequest));
        return new ApplicationResponse<>();
    }

    @Tag(name = "activity")
    @Operation(summary = "활동 모집 마감하기", description = "[토큰필요] 활동 모집 마감 시 활동장이 같이 활동할 멤버를 정한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모집이 마감되었습니다."),
            @ApiResponse(responseCode = "400", description = "모집 중인 활동이 아닙니다."),
            @ApiResponse(responseCode = "401", description = "활동장이 아닙니다."),
    })
    @PatchMapping("/{id}/close")
    public ApplicationResponse<URI> closeRecruitment(@PathVariable Long id,
                                                     @RequestBody @Valid CloseRecruitRequest closeRecruitRequest) {
        activityService.closeRecruitment(SecurityUtils.getMemberId(), id, closeRecruitRequest.getLoginIdList());
        URI uri = UriCreator.createURI(DEFAULT_URL, id);
        return new ApplicationResponse<>(uri);
    }

    @Tag(name = "activity")
    @Operation(summary = "활동 마무리하기", description = "[토큰필요] 소기의 목적을 달성하고 활동을 끝낸다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "활동이 종료되었습니다."),
            @ApiResponse(responseCode = "400", description = "진행 중인 활동이 아닙니다."),
            @ApiResponse(responseCode = "401", description = "활동장이 아닙니다.")
    })
    @ResponseStatus(OK)
    @PatchMapping("/{id}/finish")
    public ApplicationResponse<URI> finishActivity(@PathVariable Long id) {
        activityService.finishActivity(SecurityUtils.getMemberId(), id);
        URI uri = UriCreator.createURI(DEFAULT_URL, id);
        return new ApplicationResponse<>(uri);
    }

    @Tag(name = "activity")
    @Operation(summary = "활동 삭제하기", description = "[토큰필요] 활동장만 모집 중인 활동을 삭제할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "활동이 삭제되었습니다."),
            @ApiResponse(responseCode = "401", description = "활동장이 아닙니다.")
    })
    @ResponseStatus(OK)
    @DeleteMapping("/{id}")
    public ApplicationResponse deleteActivity(@PathVariable Long id) {
        activityService.delete(SecurityUtils.getMemberId(), id);
        return new ApplicationResponse<>();
    }

    @Tag(name = "activity")
    @Operation(summary = "활동 신청 취소하기", description = "[토큰필요] 신청한 활동 취소하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "신청이 취소되었습니다."),
            @ApiResponse(responseCode = "404", description = "존재하는 신청 내역이 없습니다.")
    })
    @ResponseStatus(OK)
    @DeleteMapping("/{id}/apply")
    public ApplicationResponse cancelApply(@PathVariable Long id) {
        activityService.cancelApply(SecurityUtils.getMemberId(), id);
        return new ApplicationResponse<>();
    }
}
