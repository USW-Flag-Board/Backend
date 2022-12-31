package com.FlagHome.backend.domain.report.Service;

import com.FlagHome.backend.domain.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RepostService {
    private final ReportRepository reportRepository;
}
