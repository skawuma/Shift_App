package com.skawuma.shiftapp.service;

import com.skawuma.shiftapp.dto.ShiftRequestDto;
import com.skawuma.shiftapp.model.ShiftRequest;
import com.skawuma.shiftapp.model.RequestStatus;
import com.skawuma.shiftapp.repository.ShiftRequestRepository;
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
public class ShiftRequestService {

    private final ShiftRequestRepository repo;

    public ShiftRequestService(ShiftRequestRepository repo) { this.repo = repo; }

    public ShiftRequest create(ShiftRequestDto dto) {
        ShiftRequest r = new ShiftRequest();
        r.setEmployeeName(dto.getEmployeeName());
        r.setRequestedDays(dto.getRequestedDays());
        r.setShift(dto.getShift());
        r.setStatus(RequestStatus.PENDING);
        return repo.save(r);
    }

    public List<ShiftRequest> findAll() { return repo.findAll(); }

    public Optional<ShiftRequest> findById(Long id) { return repo.findById(id); }

    public ShiftRequest updateStatus(Long id, RequestStatus status, String comment) {
        ShiftRequest r = repo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        r.setStatus(status);
        r.setAdminComment(comment);
        return repo.save(r);
    }
}
