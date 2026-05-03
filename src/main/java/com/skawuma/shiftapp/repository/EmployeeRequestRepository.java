package com.skawuma.shiftapp.repository;

import com.skawuma.shiftapp.model.EmployeeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author samuelkawuma
 * @package com.skawuma.shiftapp.repository
 * @project Shift-App
 * @date 10/9/25
 */
@Repository
public interface EmployeeRequestRepository  extends JpaRepository<EmployeeRequest, Long> {
}
