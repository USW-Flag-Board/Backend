package com.FlagHome.backend.domain.activity.mapper;

import com.FlagHome.backend.domain.activity.controller.dto.request.CreateActivityRequest;
import com.FlagHome.backend.domain.activity.controller.dto.request.ActivityInfoRequest;
import com.FlagHome.backend.domain.activity.controller.dto.request.UpdateActivityRequest;
import com.FlagHome.backend.domain.activity.controller.dto.response.ActivityDetailResponse;
import com.FlagHome.backend.domain.activity.entity.Activity;
import com.FlagHome.backend.domain.activity.entity.ActivityInfo;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL
)
public interface ActivityMapper {
    @Mapping(source = "createActivityRequest", target = "info", qualifiedByName = "toInfo")
    @Mapping(target = "leader", ignore = true)
    Activity toActivity(CreateActivityRequest createActivityRequest);

    @Mapping(source = "updateActivityRequest", target = "info", qualifiedByName = "toInfo")
    @Mapping(target = "leader", ignore = true)
    @Mapping(target = "type", ignore = true)
    Activity toActivity(UpdateActivityRequest updateActivityRequest);

    @Mapping(source = "activity.leader.name", target = "leader")
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
