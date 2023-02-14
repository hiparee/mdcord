package com.lemon.mdcord.common.config;

import com.lemon.mdcord.common.security.jwt.JwtAccessDeniedHandler;
import com.lemon.mdcord.common.security.jwt.JwtAuthenticationEntryPoint;
import com.lemon.mdcord.common.security.jwt.JwtFilterConfigurer;
import com.lemon.mdcord.common.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtProvider jwtProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .antMatchers(
//                        "/**/js/**", "/**/css/**", "/static/**"
                );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .formLogin().disable()
                .csrf().disable()
                .cors()
            .and()
                .headers()
                .frameOptions()
                .sameOrigin()
            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .authorizeRequests()
//                    .antMatchers(HttpMethod.POST, "/api/members/signin").permitAll()
//                    .antMatchers(HttpMethod.GET, "/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**", "/", "/**/js/**", "/**/css/**", "/index.html").permitAll()
//                    .anyRequest().authenticated()
                .antMatchers("/**").permitAll()
            .and()
                .exceptionHandling()
                    .accessDeniedHandler(jwtAccessDeniedHandler)
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .and()
                .apply(new JwtFilterConfigurer(jwtProvider));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

//        configuration.addAllowedOrigin("http://localhost:8081");
//        configuration.addAllowedOrigin("http://127.0.0.1:5500");
//        configuration.addAllowedHeader("*");
//        configuration.addAllowedMethod("*");

        configuration.setAllowedOrigins(List.of("http://localhost:8081", "http://127.0.0.1:5500"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowedMethods(List.of("POST", "GET", "PUT"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}
