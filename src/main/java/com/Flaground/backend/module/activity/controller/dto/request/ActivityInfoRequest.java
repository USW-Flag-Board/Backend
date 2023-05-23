package com.Flaground.backend.module.activity.controller.dto.request;

import com.Flaground.backend.module.activity.domain.enums.BookUsage;
import com.Flaground.backend.module.activity.domain.enums.Proceed;

public interface ActivityInfoRequest {
    Proceed getProceed();

    String getGithubURL();

    BookUsage getBookUsage();

    String getBookName();
}
