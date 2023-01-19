package com.FlagHome.backend.domain.report.entity;

import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.report.ReportType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "reporter_id")
    private Member reporter;

    @OneToOne
    @JoinColumn(name = "reported_id")
    private Member reported;
    @Column
    private String reportedURL;

    @Column
    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    @Column
    private String detailReason;

    @Column
    private LocalDateTime createdAt;
}
