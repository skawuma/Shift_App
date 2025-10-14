package com.skawuma.shiftapp.service;

import com.skawuma.shiftapp.dto.AuthRequest;
import com.skawuma.shiftapp.dto.AuthResponse;
import com.skawuma.shiftapp.dto.RegisterRequest;
import com.skawuma.shiftapp.dto.UserDto;
import com.skawuma.shiftapp.model.User;
import com.skawuma.shiftapp.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/**
 * @author samuelkawuma
 * @package com.skawuma.shiftapp.service
 * @project Shift-App
 * @date 10/12/25
 */
@Service
public class AuthService {

    private final UserRepository userRepo;
    private final BCryptPasswordEncoder encoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepo, BCryptPasswordEncoder encoder, JwtService jwtService) {
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.jwtService = jwtService;
    }

    public AuthResponse login(AuthRequest req) {
        Optional<User> uOpt = userRepo.findByUsername(req.getUsername());
        if (uOpt.isEmpty()) throw new RuntimeException("Invalid credentials");
        User u = uOpt.get();
        System.out.println("UserPassword Provided: " + req.getPassword());
        System.out.println("UserPassword while logged in: " + u.getPassword());
        System.out.println("Password matches: " + encoder.matches(req.getPassword(), u.getPassword()));
        System.out.println(new BCryptPasswordEncoder().encode(req.getPassword()));

        if (!encoder.matches(req.getPassword(), u.getPassword())) throw new RuntimeException("Invalid credentials");


        String token = jwtService.generateToken(u.getUsername(), Map.of("role", u.getRole(), "userId", u.getId()));
        System.out.println("Token: " + token);
        return new AuthResponse(token, u.getRole(), u.getId(), u.getEmail());
    }

    public UserDto register(RegisterRequest req) {
        if (userRepo.existsByUsername(req.getUsername())) throw new RuntimeException("Username exists");
        if (userRepo.existsByEmail(req.getEmail())) throw new RuntimeException("Email exists");
        User u = new User();
        u.setUsername(req.getUsername());
        u.setEmail(req.getEmail());
        u.setPassword(encoder.encode(req.getPassword()));
        u.setRole("EMPLOYEE");
        User saved = userRepo.save(u);
        UserDto dto = new UserDto();
        dto.setId(saved.getId());
        dto.setUsername(saved.getUsername());
        dto.setEmail(saved.getEmail());
        dto.setRole(saved.getRole());
        return dto;
    }
}
