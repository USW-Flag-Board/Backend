package com.FlagHome.backend.domain.activity.entity;

import com.FlagHome.backend.domain.activity.Status;
import com.FlagHome.backend.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Study extends Activity {

    @Builder
    public Study(Long id, String name, String description, String period,
                 String etc, Member leader, List<Member> members, Status status) {
        super(id, name, description, period, etc, leader, members, status);
    }
}
