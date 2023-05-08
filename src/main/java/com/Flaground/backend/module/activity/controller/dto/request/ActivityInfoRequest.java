package com.Flaground.backend.module.activity.controller.dto.request;

import com.Flaground.backend.module.activity.entity.enums.BookUsage;
import com.Flaground.backend.module.activity.entity.enums.Proceed;

public interface ActivityInfoRequest {
    Proceed getProceed();

    String getGithubURL();

    BookUsage getBookUsage();

    String getBookName();
}
