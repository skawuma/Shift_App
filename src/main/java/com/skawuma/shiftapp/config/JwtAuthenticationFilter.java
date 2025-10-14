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

    private final JwtService jwtService;
    private final UserRepository userRepo;

    public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepo) {
        this.jwtService = jwtService;
        this.userRepo = userRepo;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1️⃣ Get the Authorization header
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 2️⃣ Skip if there’s no Bearer token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        try {
            // 3️⃣ Extract username from token
            String username = jwtService.extractUsername(token);
            Optional<User> userOpt = userRepo.findByUsername(username);

            // 4️⃣ If user found and valid, authenticate
            if (userOpt.isPresent()) {
                User user = userOpt.get();

                var authentication = new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
                );

                // 5️⃣ Set authentication in context for this request
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            // Invalid token — we don’t stop the chain (graceful fail)
        }

        // 6️⃣ Continue the request
        filterChain.doFilter(request, response);


    }
}
