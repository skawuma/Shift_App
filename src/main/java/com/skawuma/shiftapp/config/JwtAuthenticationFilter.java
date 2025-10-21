package com.skawuma.shiftapp.config;

import com.skawuma.shiftapp.model.User;
import com.skawuma.shiftapp.repository.UserRepository;
import com.skawuma.shiftapp.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * @author samuelkawuma
 * @package com.skawuma.shiftapp.config
 * @project Shift-App
 * @date 10/12/25
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER = "Bearer ";
    private final JwtService jwtService;
    private final UserRepository userRepo;
    private final AntPathMatcher matcher = new AntPathMatcher();

    public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepo) {
        this.jwtService = jwtService;
        this.userRepo = userRepo;
    }

    // Skip authentication on public endpoints for performance
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return matcher.match("/api/auth/**", path)
                || matcher.match("/actuator/health", path)
                || matcher.match("/error", path);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith(BEARER)) {
            String token = authHeader.substring(BEARER.length());
            try {
                String username = jwtService.extractUsername(token);

                if (username != null) {
                    Optional<User> userOpt = userRepo.findByUsername(username);
                    if (userOpt.isPresent() && jwtService.isTokenValid(token, userOpt.get())) {
                        User user = userOpt.get();

                        var auth = new UsernamePasswordAuthenticationToken(
                                user.getUsername(),
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
                        );

                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            } catch (Exception ignored) {
                // If token is invalid/expired, leave context unauthenticated.
                // Downstream will return 403 via the entrypoint configured.
            }
        }

        chain.doFilter(request, response);
    }
}
