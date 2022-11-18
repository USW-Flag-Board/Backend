package com.FlagHome.backend.v1.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity //spring security 를 적용
public class SecurityConfig{

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeRequests().antMatchers("/**").permitAll()
             .and()
                .formLogin()
                .loginPage("/login")   // 로그인페이지 url 뭐라고 지정하죠..?
                .defaultSuccessUrl("/")
        ;
        return http.build();
    }

}
