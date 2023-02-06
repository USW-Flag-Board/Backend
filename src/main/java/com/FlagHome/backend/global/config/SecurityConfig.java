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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

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
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://flaground.netlify.app"));
        configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "PATCH", "DELETE"));
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

                .antMatchers(HttpMethod.GET, "/api/activities", "/api/activities/{id}").permitAll()
                .antMatchers("/api/activities/**").hasAnyRole("CREW")

                .antMatchers(HttpMethod.GET, "/api/boards/**").permitAll()
                .antMatchers("api/boards/**").hasRole("ADMIN")

                .antMatchers(HttpMethod.GET, "/api/members/{lgoinId}").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/members/find/password").permitAll()
                .antMatchers(HttpMethod.POST, "/api/members/**").permitAll()
                .antMatchers("/api/members/**").hasAnyRole("USER", "CREW")

                .antMatchers(HttpMethod.GET, "/api/posts/**").permitAll()
                .antMatchers("/api/posts/**").hasAnyRole("USER", "CREW")

                .antMatchers(HttpMethod.GET, "/api/replies/**").permitAll()
                .antMatchers("/api/replies/**").hasAnyRole("USER", "CREW")

                .antMatchers(HttpMethod.POST, "api/reports/**").hasAnyRole("USER", "CREW")
                .antMatchers("api/reports/**").hasRole("ADMIN")

                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated();

        http
                .apply(new JwtSecurityConfig(jwtUtilizer));

        return http.build();
    }
}
