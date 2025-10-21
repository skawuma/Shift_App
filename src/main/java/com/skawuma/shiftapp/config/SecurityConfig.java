package com.skawuma.shiftapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * @author samuelkawuma
 * @package com.skawuma.shiftapp.config
 * @project Shift-App
 * @date 10/12/25
 */

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Stateless JWT
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // CORS tuned for your domains
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Basic hardening headers
                .headers(headers -> headers
                        .httpStrictTransportSecurity(hsts -> hsts.includeSubDomains(true).maxAgeInSeconds(31536000))
                        .xssProtection(Customizer.withDefaults())
                        .contentTypeOptions(Customizer.withDefaults())
                        .frameOptions(fo -> fo.deny())
                        .referrerPolicy(rp -> rp.policy(org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER))
                )

                // Public vs protected endpoints
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**",
                                "/error",
                                "/swagger-ui/**", "/v3/api-docs/**" // if you use Swagger
                        ).permitAll()
                        // (optional) make health info public if you expose it
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().authenticated()
                )

                // 401/403 JSON responses (not redirects)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new Http403ForbiddenEntryPoint()) // returns 403 if no/invalid auth
                        .accessDeniedHandler(new AccessDeniedHandlerImpl())         // returns 403 if insufficient role
                )

                // Register JWT filter before UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    /**
     * CORS for local dev + production subdomain.
     * Because you’re using Authorization headers (JWT), credentials are not needed.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();

        // Allow dev hosts and your production subdomain
        cfg.setAllowedOrigins(List.of(
                "http://localhost:4200",
                "http://127.0.0.1:4200",
                "https://schedule.samuelkawuma.com",
                "http://10.0.0.98:4200",
                "http://samuels-macbook-pro.local:4200"
        ));

        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With"));
        cfg.setExposedHeaders(List.of("Authorization")); // if you ever reissue tokens in headers
        cfg.setAllowCredentials(false); // JWT in header -> not needed; safer off
        cfg.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }
}
