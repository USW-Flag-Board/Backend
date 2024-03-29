package com.Flaground.backend.global.config.security;

import com.Flaground.backend.global.config.JwtSecurityConfig;
import com.Flaground.backend.global.jwt.JwtAccessDeniedHandler;
import com.Flaground.backend.global.jwt.JwtAuthenticationEntryPoint;
import com.Flaground.backend.global.jwt.JwtUtilizer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static com.Flaground.backend.module.member.domain.enums.Role.*;

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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:3000", "https://flaground.vercel.app"));
        configuration.setAllowedMethods(List.of("POST", "GET", "PUT", "PATCH", "DELETE"));
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    // todo : request 정리하기
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .httpBasic().disable()
            .formLogin().disable()
            .logout().disable()
            .headers().frameOptions().disable()
            .and()
            .cors().configurationSource(corsConfigurationSource())
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.exceptionHandling()
            .accessDeniedHandler(jwtAccessDeniedHandler)
            .authenticationEntryPoint(jwtAuthenticationEntryPoint);

        http.authorizeRequests()
            .antMatchers("/auth/**", "/boards/**", "/members/find/password", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**", "/favicon.ico").permitAll()
            .antMatchers(HttpMethod.GET, whiteGetList()).permitAll()
            .antMatchers(HttpMethod.POST, "/members/certification", "/members/find/id").permitAll()
            .antMatchers("/activities/**").hasRole(ROLE_CREW.getRole())
            .antMatchers("/reports/**", "/posts/**", "/members/**", "/images/**").hasAnyRole(ROLE_USER.getRole(), ROLE_CREW.getRole())
            .antMatchers("/admin/**").hasRole(ROLE_ADMIN.getRole())
            .anyRequest().authenticated();

        http
            .apply(JwtSecurityConfig.from(jwtUtilizer));

        return http.build();
    }

    private String[] whiteGetList() {
        return new String[]{
                "/activities", "/activities/{id}", "/activities/{loginId}/profile", "/activities/search",
                "/members/{loginId}", "/members/search",
                "/posts/**"};
    }
}
