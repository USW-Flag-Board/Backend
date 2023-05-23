package com.Flaground.backend.module.report.controller;

import com.Flaground.backend.global.common.response.ApplicationResponse;
import com.Flaground.backend.global.utility.SecurityUtils;
import com.Flaground.backend.module.report.controller.dto.request.ContentReportRequest;
import com.Flaground.backend.module.report.controller.dto.request.MemberReportRequest;
import com.Flaground.backend.module.report.controller.mapper.ReportMapper;
import com.Flaground.backend.module.report.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@Tag(name = "report", description = "신고 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportController {
    private final ReportService reportService;
    private final ReportMapper reportMapper;

    @Tag(name = "report")
    @Operation(summary = "멤버 신고하기", description = "[토큰필요] 멤버 프로필에서 신고할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "신고 완료"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자입니다."),
            @ApiResponse(responseCode = "409", description = "이미 신고한 대상입니다.")
    })
    @ResponseStatus(CREATED)
    @PostMapping("/member")
    public ApplicationResponse reportMember(@RequestBody @Valid MemberReportRequest request) {
        reportService.reportMember(SecurityUtils.getMemberId(), reportMapper.mapFrom(request));
        return new ApplicationResponse<>();
    }

    @Tag(name = "report")
    @Operation(summary = "게시글/댓글 신고하기", description = "[토큰필요] type을 지정해줘야한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "신고 완료"),
            @ApiResponse(responseCode = "409", description = "이미 신고한 대상입니다.")
    })
    @ResponseStatus(CREATED)
    @PostMapping("/content")
    public ApplicationResponse reportContent(@RequestBody @Valid ContentReportRequest request) {
        reportService.reportContent(SecurityUtils.getMemberId(), reportMapper.mapFrom(request));
        return new ApplicationResponse<>();
    }
}
