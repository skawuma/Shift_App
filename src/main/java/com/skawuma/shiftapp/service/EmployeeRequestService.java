package com.skawuma.shiftapp.service;

import com.skawuma.shiftapp.dto.CreateRequestDto;
import com.skawuma.shiftapp.model.EmployeeRequest;
import com.skawuma.shiftapp.model.RequestStatus;
import com.skawuma.shiftapp.repository.EmployeeRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author samuelkawuma
 * @package com.skawuma.shiftapp.service
 * @project Shift-App
 * @date 10/9/25
 */
@Service
public class EmployeeRequestService {

    private final EmployeeRequestRepository repo;

    public EmployeeRequestService(EmployeeRequestRepository repo) { this.repo = repo; }

    public EmployeeRequest create(CreateRequestDto dto) {
        EmployeeRequest r = new EmployeeRequest();
        r.setEmployeeName(dto.getEmployeeName());
        r.setRequestedDays(dto.getRequestedDays());
        r.setShift(dto.getShift());
        r.setStatus(RequestStatus.PENDING);
        return repo.save(r);
    }

    public List<EmployeeRequest> findAll() { return repo.findAll(); }

    public Optional<EmployeeRequest> findById(Long id) { return repo.findById(id); }

    public EmployeeRequest updateStatus(Long id, RequestStatus status, String comment) {
        EmployeeRequest r = repo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        r.setStatus(status);
        r.setAdminComment(comment);
        return repo.save(r);
    }
}
