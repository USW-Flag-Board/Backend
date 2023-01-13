package com.FlagHome.backend.domain.activity.entity;

import com.FlagHome.backend.domain.activity.Proceed;
import com.FlagHome.backend.domain.activity.Status;
import com.FlagHome.backend.domain.member.entity.Member;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
public class Study extends Activity {

    @Builder
    public Study(Long id, String name, String description, Member leader, Proceed proceed,
                 String githubLink, Boolean isBookExist, String bookName, Status status) {
        super(id, name, description, leader, proceed, githubLink, isBookExist, bookName, status);
    }
}
