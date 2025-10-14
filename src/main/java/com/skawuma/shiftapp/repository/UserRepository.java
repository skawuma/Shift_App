package com.skawuma.shiftapp.repository;

import com.skawuma.shiftapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author samuelkawuma
 * @package com.skawuma.shiftapp.repository
 * @project Shift-App
 * @date 10/12/25
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
