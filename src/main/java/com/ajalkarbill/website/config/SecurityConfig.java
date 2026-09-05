package com.ajalkarbill.website.config;

import com.ajalkarbill.website.security.JwtAuthenticationFilter;
import com.ajalkarbill.website.security.WebsiteUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final WebsiteUserDetailsService websiteUserDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
            WebsiteUserDetailsService websiteUserDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.websiteUserDetailsService = websiteUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(websiteUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(auth -> auth
                        // Root path and error pages - No authentication required
                        .requestMatchers("/", "/error").permitAll()

                        // Swagger UI and API Docs - No authentication required
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html",
                                "/swagger-ui/index.html")
                        .permitAll()

                        // Public APIs - No authentication required
                        .requestMatchers("/api/public/**", "/api/faqs/**", "/api/features/**", "/api/modules/**",
                                "/api/testimonials/**", "/api/downloads/**")
                        .permitAll()
                        .requestMatchers("/api/contact-enquiries/**", "/api/enquiries/**")
                        .permitAll()
                        .requestMatchers("/api/leads/**")
                        .permitAll()

                        // Public Analytics APIs - No authentication required for visitor tracking
                        .requestMatchers("/api/public/analytics/**")
                        .permitAll()

                        // Existing visitor tracking APIs - No authentication required
                        .requestMatchers("/api/visitors/**", "/api/visitors/activity/**")
                        .permitAll()

                        // Public Auth - Login, Register, Forgot Password, Reset Password endpoints
                        .requestMatchers("/api/auth/**").permitAll()

                        // Admin Auth - Login endpoint
                        .requestMatchers("/api/admin/auth/login").permitAll()

                        // Admin APIs - Authentication required
                        .requestMatchers("/api/admin/**").authenticated()

                        // Any other request - deny
                        .anyRequest().denyAll())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
