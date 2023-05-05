package com.FlagHome.backend.module.activity.controller.dto.request;

import com.FlagHome.backend.module.activity.entity.enums.BookUsage;
import com.FlagHome.backend.module.activity.entity.enums.Proceed;

public interface ActivityInfoRequest {
    Proceed getProceed();

    String getGithubURL();

    BookUsage getBookUsage();

    String getBookName();
}
