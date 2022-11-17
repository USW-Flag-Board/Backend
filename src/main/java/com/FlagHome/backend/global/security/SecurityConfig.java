package com.FlagHome.backend.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    private static final String[] whiteListURI = { "/**" };
//    private static final String[] needJWTFilter = { "/" };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf().disable()
            .httpBasic().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
            .antMatchers(whiteListURI).permitAll();

        http.formLogin()
            .loginPage("/login")
            .defaultSuccessUrl("/");

        return http.build();
    }

//    @Bean
//    public BCryptPasswordEncoder encodPassword() {
//        return new BCryptPasswordEncoder();
//    }

}
