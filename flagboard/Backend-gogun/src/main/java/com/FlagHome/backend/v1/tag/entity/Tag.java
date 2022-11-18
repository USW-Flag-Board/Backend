package com.FlagHome.backend.v1.tag.entity;

import com.FlagHome.backend.v1.BaseEntity;
import com.FlagHome.backend.v1.user.entity.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tag extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String description;

}
