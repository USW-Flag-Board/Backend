package com.Flaground.backend.module.member.domain;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Embeddable
public class IssueRecord {
    private static final int START_VALUE = 0;

    @Column(name = "login_failed")
    private int loginFailCount;

    @Column(name = "reported_count")
    private int reportedCount;

    @Column(name = "penalty_point")
    private int penaltyPoint;

    public IssueRecord() {
        this.loginFailCount = START_VALUE;
        this.reportedCount = START_VALUE;
        this.penaltyPoint = START_VALUE;
    }

    public int loginFail() {
        return ++this.loginFailCount;
    }

    public void resetLoginFailCount() {
        this.loginFailCount = START_VALUE;
    }

    public void applyPenalty(int penaltyPoint) {
        this.penaltyPoint += penaltyPoint;
        this.reportedCount++;
    }
}
