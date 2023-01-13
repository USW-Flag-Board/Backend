package com.FlagHome.backend.domain.activity.entity;

import com.FlagHome.backend.domain.activity.ProceedType;
import com.FlagHome.backend.domain.activity.Status;
import com.FlagHome.backend.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Builder
@Getter
@NoArgsConstructor
public class Study extends Activity {
    @Column
    private Boolean isBookExist;

    @Column
    private String bookName;

    @Builder
    public Study(Long id, String name, String description, Member leader, ProceedType proceedType,
                 Status status, Boolean isBookExist, String bookName) {
        super(id, name, description, leader, proceedType, status);
        this.isBookExist = isBookExist;
        this.bookName = bookName;
    }
}
