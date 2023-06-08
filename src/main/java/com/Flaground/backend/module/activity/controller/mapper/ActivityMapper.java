package com.Flaground.backend.module.activity.controller.mapper;

import com.Flaground.backend.module.activity.controller.dto.request.ActivityInfoRequest;
import com.Flaground.backend.module.activity.controller.dto.request.CreateActivityRequest;
import com.Flaground.backend.module.activity.controller.dto.request.UpdateActivityRequest;
import com.Flaground.backend.module.activity.domain.ActivityData;
import com.Flaground.backend.module.activity.domain.ActivityInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface ActivityMapper {
    @Mapping(source = "createActivityRequest", target = "info", qualifiedByName = "toInfo")
    ActivityData mapFrom(CreateActivityRequest createActivityRequest);

    @Mapping(source = "updateActivityRequest", target = "info", qualifiedByName = "toInfo")
    @Mapping(target = "type", ignore = true)
    ActivityData mapFrom(UpdateActivityRequest updateActivityRequest);

    @Named("toInfo")
    static ActivityInfo toInfo(ActivityInfoRequest activityInfoRequest) {
        return ActivityInfo.builder()
                .proceed(activityInfoRequest.getProceed())
                .githubURL(activityInfoRequest.getGithubURL())
                .bookUsage(activityInfoRequest.getBookUsage())
                .bookName(activityInfoRequest.getBookName())
                .build();
    }
}
