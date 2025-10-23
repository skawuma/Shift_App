package com.skawuma.shiftapp.controller;

import com.skawuma.shiftapp.dto.AuthRequest;
import com.skawuma.shiftapp.dto.AuthResponse;
import com.skawuma.shiftapp.dto.RegisterRequest;
import com.skawuma.shiftapp.service.AuthService;
import com.skawuma.shiftapp.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author samuelkawuma
 * @package com.skawuma.shiftapp.controller
 * @project Shift-App
 * @date 10/12/25
 */

@RestController
@RequestMapping("/api/auth")
//@CrossOrigin(origins = {"http://10.0.0.98:4200","http://192.168.25.119:4200"})


public class AuthController {
    private  final AuthService authService;
    private final EmailService emailService;

    public AuthController(AuthService authService, EmailService emailService) {
        this.authService = authService;
        this.emailService = emailService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        AuthResponse resp = authService.login(req);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        var userDto = authService.register(req);
        // notify admin(s)
        emailService.sendSimple("admin@example.com", "New registration", "New user: " + userDto.getUsername() + " (" + userDto.getEmail() + ")");
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/api/auth/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }

}
