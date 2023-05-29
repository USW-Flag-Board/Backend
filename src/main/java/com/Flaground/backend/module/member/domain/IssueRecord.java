package com.Flaground.backend.module.member.domain;

import com.Flaground.backend.module.member.domain.BlackState.Ban;
import com.Flaground.backend.module.member.domain.BlackState.BlackState;
import com.Flaground.backend.module.member.domain.BlackState.Watching;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Embeddable
public class IssueRecord {
    private static final int BAN_POINT = 10;
    private static final int MAX_LOGIN_FAIL = 5;
    private static final int DEFAULT_VALUE = 0;

    @Column(name = "login_failed")
    private int loginFailCount;

    @Column(name = "reported_count")
    private int reportedCount;

    @Column(name = "penalty_point")
    private int penalty;

    @Builder
    public IssueRecord(int loginFailCount, int reportedCount, int penalty) {
        this.loginFailCount = loginFailCount;
        this.reportedCount = reportedCount;
        this.penalty = penalty;
    }

    public IssueRecord() {
        this.loginFailCount = DEFAULT_VALUE;
        this.reportedCount = DEFAULT_VALUE;
        this.penalty = DEFAULT_VALUE;
    }

    public boolean isMaxLoginFailCount() {
        return this.loginFailCount == MAX_LOGIN_FAIL;
    }

    public void increaseFailCount() {
        this.loginFailCount++;
    }

    public void resetLoginFailCount() {
        this.loginFailCount = DEFAULT_VALUE;
    }

    public int applyPenalty(int penalty) {
        this.reportedCount++;
        return this.penalty += penalty;
    }

    public BlackState blackState() {
        if (this.penalty < BAN_POINT) {
            return new Watching();
        }
        return new Ban();
    }
}
