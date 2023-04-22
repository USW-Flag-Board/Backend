package com.FlagHome.backend.domain.activity.mapper;

import com.FlagHome.backend.domain.activity.controller.dto.request.ActivityRequest;
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
    @Mapping(source = "activityRequest", target = "info", qualifiedByName = "toInfo")
    @Mapping(target = "leader", ignore = true)
    Activity toActivity(ActivityRequest activityRequest);

    @Mapping(source = "activity.leader.name", target = "leader")
    ActivityDetailResponse toDetailResponse(Activity activity);

    @Named("toInfo")
    static ActivityInfo toInfo(ActivityRequest activityRequest) {
        return ActivityInfo.builder()
                .proceed(activityRequest.getProceed())
                .githubURL(activityRequest.getGithubLink())
                .bookUsage(activityRequest.getBookUsage())
                .bookName(activityRequest.getName())
                .build();
    }
}
