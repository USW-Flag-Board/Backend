package com.Flaground.backend.module.admin.controller;

import com.Flaground.backend.global.common.ApplicationResponse;
import com.Flaground.backend.module.admin.service.AdminService;
import com.Flaground.backend.module.auth.controller.dto.response.SignUpRequestResponse;
import com.Flaground.backend.module.board.controller.dto.request.BoardRequest;
import com.Flaground.backend.module.member.controller.dto.response.LoginLogResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "admin", description = "관리자 API")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @Tag(name = "admin")
    @ResponseStatus(OK)
    @GetMapping("/join-requests")
    public ApplicationResponse<List<SignUpRequestResponse>> getSignUpRequests() {
        List<SignUpRequestResponse> responses = adminService.getSignUpRequests();
        return new ApplicationResponse<>(responses);
    }

    @Tag(name = "admin")
    @ResponseStatus(OK)
    @GetMapping("/members/login-log")
    public ApplicationResponse<List<LoginLogResponse>> getLoginLogs() {
        List<LoginLogResponse> responses = adminService.getLoginLogs();
        return new ApplicationResponse<>(responses);
    }

    @Tag(name = "admin")
    @ResponseStatus(OK)
    @PostMapping("/join-requests/{id}/approval")
    public ApplicationResponse approveSignUp(@PathVariable("id") Long authInformationId) {
        adminService.approvSignUp(authInformationId);
        return new ApplicationResponse<>();
    }

    @Tag(name = "admin")
    @ResponseStatus(OK)
    @DeleteMapping("/join-requests/{id}/rejection")
    public ApplicationResponse rejectSignUp(@PathVariable("id") Long authInformationId) {
        adminService.rejectSignUp(authInformationId);
        return new ApplicationResponse<>();
    }

    @Tag(name = "admin")
    @Operation(summary = "게시판 생성하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시판 생성 완료"),
            @ApiResponse(responseCode = "401", description = "관리자가 아닙니다."),
            @ApiResponse(responseCode = "409", description = "이미 같은 게시판이 존재합니다.")
    })
    @ResponseStatus(CREATED)
    @PostMapping("/board")
    public ApplicationResponse create(@RequestBody @Valid BoardRequest boardRequest) {
        adminService.createBoard(boardRequest.toEntity());
        return new ApplicationResponse<>();
    }

    @Tag(name = "admin")
    @Operation(summary = "게시판 수정하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시판 수정 완료"),
            @ApiResponse(responseCode = "400", description = "올바르지 않는 게시판입니다."),
            @ApiResponse(responseCode = "401", description = "관리자가 아닙니다."),
            @ApiResponse(responseCode = "409", description = "이미 같은 게시판이 존재합니다.")
    })
    @ResponseStatus(OK)
    @PutMapping("/board/{name}")
    public ApplicationResponse update(@PathVariable("name") String boardName,
                                      @RequestBody @Valid BoardRequest boardRequest) {
        adminService.updateBoard(boardName, boardRequest.toEntity());
        return new ApplicationResponse<>();
    }

    @Tag(name = "admin")
    @Operation(summary = "게시판 삭제하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시판 삭제 완료"),
            @ApiResponse(responseCode = "400", description = "올바르지 않는 게시판입니다."),
            @ApiResponse(responseCode = "401", description = "관리자가 아닙니다.")
    })
    @ResponseStatus(OK)
    @DeleteMapping("/board/{name}")
    public ApplicationResponse delete(@PathVariable("name") String boardName) {
        adminService.deleteBoard(boardName);
        return new ApplicationResponse<>();
    }
}