package com.skawuma.shiftapp.repository;

import com.skawuma.shiftapp.model.ShiftRequest;
import com.skawuma.shiftapp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

/**
 * @author samuelkawuma
 * @package com.skawuma.shiftapp.repository
 * @project Shift-App
 * @date 10/9/25
 */
@Repository
public interface ShiftRequestRepository extends JpaRepository<ShiftRequest, Long> {
    boolean existsByEmployeeAndRequestedDatesContainsAndShift(User employee, LocalDate date, String shift);

    Page<ShiftRequest> findByStatus(String status, Pageable pageable);
    Page<ShiftRequest> findByEmployee_UsernameContainingIgnoreCase(String username, Pageable pageable);
}
