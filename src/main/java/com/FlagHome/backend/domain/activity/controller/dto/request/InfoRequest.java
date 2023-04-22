package com.FlagHome.backend.domain.activity.controller.dto.request;

import com.FlagHome.backend.domain.activity.entity.enums.BookUsage;
import com.FlagHome.backend.domain.activity.entity.enums.Proceed;

public interface InfoRequest {
    Proceed getProceed();

    String getGithubURL();

    BookUsage getBookUsage();

    String getBookName();
}
