package com.Flaground.backend.global.config;

import com.Flaground.backend.global.config.security.filter.JwtFilter;
import com.Flaground.backend.global.jwt.JwtUtilizer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final JwtUtilizer jwtUtilizer;

    @Override
    public void configure(HttpSecurity http) {
        JwtFilter customFilter = new JwtFilter(jwtUtilizer);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
