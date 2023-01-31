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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().cors();

        http
                .exceptionHandling()
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .authenticationEntryPoint(jwtAuthenticationEntryPoint);

        http.authorizeRequests()
                .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**").permitAll()
                .antMatchers("/api/auth/**").permitAll()

                // 활동
                .antMatchers(HttpMethod.GET, "/api/activities", "/api/activities/{id}").permitAll()
                .antMatchers("/api/activities/**").hasAnyRole("CREW")

                // 게시판
                .antMatchers(HttpMethod.GET, "/api/boards/**").permitAll()
                .antMatchers("api/boards/**").hasRole("ADMIN")

                // 멤버
                .antMatchers(HttpMethod.GET, "/api/members/{lgoinId}").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/members/find/password").permitAll()
                .antMatchers(HttpMethod.POST, "/api/members/**").permitAll()
                .antMatchers("/api/members/**").hasAnyRole("USER", "CREW")

                // 게시글
                .antMatchers(HttpMethod.GET, "/api/posts/**").permitAll()
                .antMatchers("/api/posts/**").hasAnyRole("USER", "CREW")

                // 댓글
                .antMatchers(HttpMethod.GET, "/api/replies/**").permitAll()
                .antMatchers("/api/replies/**").hasAnyRole("USER", "CREW")

                // 신고
                .antMatchers(HttpMethod.POST, "api/reports/**").hasAnyRole("USER", "CREW")
                .antMatchers("api/reports/**").hasRole("ADMIN")

                // 관리자
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated();

        http
                .apply(new JwtSecurityConfig(jwtUtilizer));

        return http.build();
    }
}
