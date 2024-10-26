package com.fpu.exe.cleaninghub.config;

import com.fpu.exe.cleaninghub.exception.CustomAccessDeniedHandler;
import com.fpu.exe.cleaninghub.services.interfc.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    @Autowired
    @Lazy
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    @Lazy
    private UserService userService;

    @Autowired
    private final CustomerOAuth2UserService customOAuth2UserService;
    @Autowired
    private final LogoutService logoutHandler;
    @Autowired
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    private BeanConfig beanConfig;

    @Autowired
    public void setJwtAuthenticationFilter(@Lazy JWTAuthenticationFilter jwtAuthenticationFilter, @Lazy UserService userService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Collections.singletonList("http://localhost:5173"));
                    config.setAllowedMethods(Collections.singletonList("*"));
                    config.setAllowCredentials(true);
                    config.setAllowedHeaders(Collections.singletonList("*"));
                    config.setExposedHeaders(List.of("Authorization"));
                    config.setMaxAge(3600L);
                    return config;
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                                .requestMatchers("/api/v1/auth/**", "/api/v1/categories/getAll", "/api/v1/bookings/**", "/api/v1/user/**" , "/api/v1/services/**", "/api/v1/payOS/**" , "/api/v1/durations/**",  "/swagger-resources/**",
                                        "/webjars/**", "/swagger-ui.html", "/api/v1/vouchers/**", "/v3/api-docs/**", "/swagger-ui/**", "/oauth2/authorization/google",
                                        "/ws/**")
                                .permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(e -> e.accessDeniedHandler(customAccessDeniedHandler)
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .authenticationProvider(beanConfig.authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout.logoutUrl("/api/v1/auth/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((request, response, authentication) ->
                                SecurityContextHolder.clearContext()))
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(user -> user.userService(customOAuth2UserService))
                        .defaultSuccessUrl("/api/v1/auth/signInGoogle", true)
                        .failureUrl("http://localhost:5173/login"));

        http.exceptionHandling(exception -> exception
                .authenticationEntryPoint(beanConfig.authenticationEntryPoint())
        );
        return http.build();
    }


}
