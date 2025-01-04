package com.trickytechies.foodapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.trickytechies.foodapi.filter.JwtAuthenticationFilter;
import com.trickytechies.foodapi.service.UserService;
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/login/**", "/register/**", "/refresh_token/**").permitAll()
                        .requestMatchers("/admin_only/**").hasAuthority("ADMIN")
                        .requestMatchers("/customer/**").hasAuthority("CUSTOMER")
                        .requestMatchers("/delivery_agent/**").hasAuthority("FOOD_DELIVERY_AGENT")

                        .requestMatchers(HttpMethod.GET, "/branches/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/branches/**").hasAuthority("SUPER_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/branches/**").hasAuthority("SUPER_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/branches/**").hasAuthority("SUPER_ADMIN")

                        .requestMatchers(HttpMethod.GET, "/menu/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/menu/**").hasAnyAuthority("ADMIN", "SUPER_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/menu/**").hasAnyAuthority("ADMIN", "SUPER_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/menu/**").hasAnyAuthority("ADMIN", "SUPER_ADMIN")
        
                        .anyRequest().authenticated()
                )
                .userDetailsService(userService)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
