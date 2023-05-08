package com.Flaground.backend.module.report.controller;

import com.Flaground.backend.module.report.Report;
import com.Flaground.backend.module.report.Service.ReportService;
import com.Flaground.backend.module.report.controller.dto.ReportRequest;
import com.Flaground.backend.global.common.ApplicationResponse;
import com.Flaground.backend.global.utility.SecurityUtils;
import com.Flaground.backend.global.utility.UriCreator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.http.HttpStatus.OK;

@Tag(name = "report", description = "신고 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportController {
    private static final String DEFAULT_URL = "/reports";
    private final ReportService reportService;

    @Tag(name = "report")
    @Operation(summary = "신고하기", description = "[토큰필요] 유저, 게시글, 댓글을 대상으로 신고할 수 있다.\n" +
            "한 멤버가 한 대상(URL 기준)으로 신고할 수 있고 취소할 수 없다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "신고가 접수되었습니다."),
            @ApiResponse(responseCode = "409", description = "이미 신고한 대상입니다")
    })
    @ResponseStatus(OK)
    @PostMapping("/tmp")
    public ApplicationResponse<URI> report(@RequestBody ReportRequest reportRequest) {
        long id = reportService.create(SecurityUtils.getMemberId(), Report.from(reportRequest));
        URI location = UriCreator.createURI(DEFAULT_URL, id);
        return new ApplicationResponse(location);
    }

    @Tag(name = "report")
    @Operation(summary = "모든 신고 가져오기", description = "관리자 전용 기능, 모든 신고 내역을 가져온다는 의미")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모든 신고 가져오기 성공"),
    })
    @GetMapping
    public ResponseEntity<ApplicationResponse> getAllReports() {
        ApplicationResponse response = ApplicationResponse.of(reportService.getAllReports(), OK, "모든 신고 정보 가져오기 성공");
        return ResponseEntity.ok(response);
    }

    @Tag(name = "report")
    @Operation(summary = "신고 삭제하기", description = "관리자 전용 기능, 신고 삭제는 관리자가 신고 건에 대해서 확인했음을 의미")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "신고가 삭제되었습니다."),
            @ApiResponse(responseCode = "404", description = "신고이력이 존재하지 않습니다.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApplicationResponse> deleteReport(@PathVariable("id") long reportId){
        reportService.deleteReport(reportId);
        ApplicationResponse response = ApplicationResponse.of(null, OK, "신고가 삭제되었습니다.");
        return ResponseEntity.ok(response);
    }

    @Tag(name = "report")
    @Operation(summary = "모든 신고 삭제하기", description = "관리자 전용, 신고 삭제는 관리자가 신고 건에 대해서 확인했음을 의미")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모든 신고가 삭제되었습니다."),
    })
    @DeleteMapping
    public ResponseEntity<ApplicationResponse> deleteAllReports() {
        reportService.deleteAllReports();
        ApplicationResponse response = ApplicationResponse.of(null, OK, "모든 신고가 삭제되었습니다.");
        return ResponseEntity.ok(response);
    }
}
