package com.FlagHome.backend.domain.report.controller;

import com.FlagHome.backend.domain.report.Service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report")
public class ReportController {
    private static final String DEFAULT_URL = "/api/report";
    private final ReportService reportService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable("id") long reportId){
        reportService.deleteReport(reportId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllReports() {
        reportService.deleteAllReports();
        return ResponseEntity.ok().build();
    }


/*
    @PostMapping
    public ResponseEntity<URI> create(@RequestBody ReportRequest reportRequest) {
        long id = reportService.create(SecurityUtils.getMemberId(), ReportRequest.valueOf(reportRequest));
        URI location = UriCreator.createUri(DEFAULT_URL, id);

        return ResponseEntity.created(location).build();
    }*/
}
