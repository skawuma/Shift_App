package com.skawuma.shiftapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
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
                // ✅ New style to disable CSRF
                .csrf(AbstractHttpConfigurer::disable)

                // ✅ Enable and apply our CORS config
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // ✅ Stateless JWT-based security
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // ✅ Allow open access to auth endpoints
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/api/requests").hasRole("EMPLOYEE")
//                        .requestMatchers(HttpMethod.GET, "/api/requests/admin").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )

                // ✅ Register our custom JWT filter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        // Cost 12 matches your inserted hash ($2a$12$...)
        return new BCryptPasswordEncoder(12);
    }

    /**
     * ✅ Defines CORS settings so Angular (localhost:4200) can call backend APIs.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Origins allowed (you can list your production domain here)
        config.setAllowedOrigins(List.of("http://localhost:4200",
                //dev purpose
                "http://10.0.0.98:4200",
                "http://192.168.25.119:4200",
                "https://evergreenctr.org"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true); // Required for cookies/auth headers
        config.setMaxAge(3600L); // Cache preflight 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
