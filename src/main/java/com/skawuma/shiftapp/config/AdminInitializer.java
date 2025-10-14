package com.skawuma.shiftapp.config;

import com.skawuma.shiftapp.model.User;
import com.skawuma.shiftapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;

/**
 * @author samuelkawuma
 * @package com.skawuma.shiftapp.config
 * @project Shift-App
 * @date 10/13/25
 */

@Configuration
public class AdminInitializer {

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Bean
    CommandLineRunner initUsers(UserRepository userRepo, BCryptPasswordEncoder encoder, Environment env) {
        return args -> {
            // Create or rotate admin
            userRepo.findByUsername(adminUsername).ifPresentOrElse(admin -> {
                if (!encoder.matches(adminPassword, admin.getPassword())) {
                    admin.setPassword(encoder.encode(adminPassword));
                    userRepo.save(admin);
                    System.out.println("🔁 Admin password rotated via environment variable.");
                } else {
                    System.out.println("✅ Admin password up to date.");
                }
            }, () -> {
                // 🆕 Create default admin if not found
                User admin = new User();
                admin.setUsername(adminUsername);
                admin.setEmail("admin@example.com");
                admin.setPassword(encoder.encode(adminPassword));
                admin.setRole("ROLE_ADMIN");
                userRepo.save(admin);
                System.out.println("✅ Default admin created: " + adminUsername);
            });

            // 🧑‍💻 Only create default employee in DEV environment
            if (Arrays.asList(env.getActiveProfiles()).contains("dev")) {
                userRepo.findByUsername("employee").ifPresentOrElse(emp -> {
                    System.out.println("✅ Employee user already exists.");
                }, () -> {
                    User employee = new User();
                    employee.setUsername("employee");
                    employee.setEmail("employee@example.com");
                    employee.setPassword(encoder.encode("password123"));
                    employee.setRole("ROLE_EMPLOYEE");
                    userRepo.save(employee);
                    System.out.println("👷 Default employee created (dev-only). username=employee, password=password123");
                });
            } else {
                System.out.println("🚫 Skipping employee seed — running in non-dev profile.");
            }
        };
    }
}
