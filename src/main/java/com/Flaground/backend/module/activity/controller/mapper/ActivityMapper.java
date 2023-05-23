package com.Flaground.backend.module.activity.controller.mapper;

import com.Flaground.backend.module.activity.controller.dto.request.CreateActivityRequest;
import com.Flaground.backend.module.activity.controller.dto.request.ActivityInfoRequest;
import com.Flaground.backend.module.activity.controller.dto.request.UpdateActivityRequest;
import com.Flaground.backend.module.activity.controller.dto.response.ActivityDetailResponse;
import com.Flaground.backend.module.activity.domain.Activity;
import com.Flaground.backend.module.activity.domain.ActivityInfo;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface ActivityMapper {
    @Mapping(source = "createActivityRequest", target = "info", qualifiedByName = "toInfo")
    @Mapping(target = "leader", ignore = true)
    Activity mapFrom(CreateActivityRequest createActivityRequest);

    @Mapping(source = "updateActivityRequest", target = "info", qualifiedByName = "toInfo")
    @Mapping(target = "leader", ignore = true)
    @Mapping(target = "type", ignore = true)
    Activity mapFrom(UpdateActivityRequest updateActivityRequest);

    @Mapping(source = "leader.name", target = "leader")
    ActivityDetailResponse toDetailResponse(Activity activity);

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
