package com.FlagHome.backend.v1.user.dto;

import com.FlagHome.backend.v1.user.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private String id;

    private String password;

    private String name;

    private Role role;

}
