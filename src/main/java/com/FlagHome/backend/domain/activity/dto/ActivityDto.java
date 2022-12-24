package com.FlagHome.backend.domain.activity.dto;


import com.FlagHome.backend.domain.Status;
import com.FlagHome.backend.domain.activity.entity.Activity;
import com.FlagHome.backend.domain.member.entity.Member;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class ActivityDto {
    private long id;
    private Member member;
    private String name;
    private String discription;
    private String period;
    private Status status;

    public ActivityDto(Activity activityEntity) {
        this.id = activityEntity.getId();
        this.member = activityEntity.getMember();
        this.name = activityEntity.getName();
        this.discription = activityEntity.getDiscription();
        this.period = activityEntity.getPeriod();
        this.status = activityEntity.getStatus();
    }
}
