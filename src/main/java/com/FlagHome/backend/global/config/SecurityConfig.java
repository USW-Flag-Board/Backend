package com.FlagHome.backend.global.config;

import com.FlagHome.backend.global.jwt.JwtAccessDeniedHandler;
import com.FlagHome.backend.global.jwt.JwtAuthenticationEntryPoint;
import com.FlagHome.backend.global.jwt.JwtUtilizer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtUtilizer jwtUtilizer;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 권장하지 않는 방식 -> 추가방안 고려해보기
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .exceptionHandling()
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .authenticationEntryPoint(jwtAuthenticationEntryPoint);

//        http.authorizeRequests()
//                .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**").permitAll()
//                .antMatchers("/api/auth/**").permitAll()
//                .antMatchers(HttpMethod.GET, "/api/member/**").permitAll()
//                .antMatchers(HttpMethod.POST, "/api/member/**").permitAll()
//                .antMatchers("/api/post/**").hasAnyRole("USER", "CREW")
//                .antMatchers("/api/reply/**").hasAnyRole("USER", "CREW")
//                .antMatchers("/api/file/**").hasAnyRole("USER", "CREW")
//                .antMatchers("/api/member/**").hasAnyRole("USER", "CREW")
//                .antMatchers("/api/admin/**").hasRole("ADMIN")
//                .anyRequest().authenticated();
        //카테고리 테스트 시 오류 발생으로 주석 처리 후 추가 된 코드입니다.(23.01.09 강지은)
        http.authorizeRequests().anyRequest().permitAll();
        http
                .apply(new JwtSecurityConfig(jwtUtilizer));

        return http.build();
    }
}
