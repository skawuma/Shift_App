package com.skawuma.shiftapp.service;

import com.skawuma.shiftapp.dto.ShiftRequestDto;
import com.skawuma.shiftapp.model.ShiftRequest;
import com.skawuma.shiftapp.model.RequestStatus;
import com.skawuma.shiftapp.model.User;
import com.skawuma.shiftapp.repository.ShiftRequestRepository;
import com.skawuma.shiftapp.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author samuelkawuma
 * @package com.skawuma.shiftapp.service
 * @project Shift-App
 * @date 10/9/25
 */
@Service
public class ShiftRequestService {

    private final ShiftRequestRepository repo;
    private final UserRepository userRepo;
    private final EmailService emailService;

    public ShiftRequestService(ShiftRequestRepository repo, UserRepository userRepo, EmailService emailService) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.emailService = emailService;
    }

    public ShiftRequestDto submit(ShiftRequestDto dto) {
        User u = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        ShiftRequest sr = new ShiftRequest();
        sr.setEmployee(u);
        sr.setRequestedDays(dto.getRequestedDays());
        sr.setShift(dto.getShift());
        sr.setStatus("PENDING");
        ShiftRequest saved = repo.save(sr);
        return toDto(saved);
    }

    public Page<ShiftRequestDto> listAll(int page, int size, String status) {
        Pageable p = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<ShiftRequest> pageEnt;
        if (status == null || status.isBlank()) pageEnt = repo.findAll(p);
        else pageEnt = repo.findByStatus(status, p);
        return pageEnt.map(this::toDto);
    }

    public List<ShiftRequestDto> listByUser(Long userId) {
        // simple: fetch all and filter (or add repository method)
        return repo.findAll().stream()
                .filter(r -> r.getEmployee().getId().equals(userId))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ShiftRequestDto updateStatus(Long id, String status, String adminComment) {
        ShiftRequest r = repo.findById(id).orElseThrow(() -> new RuntimeException("Request not found"));
        r.setStatus(status);
        r.setAdminComment(adminComment);
        ShiftRequest saved = repo.save(r);

        // send email
        String to = saved.getEmployee().getEmail();
        String subj = "Shift Request " + saved.getId() + " - " + status;
        String body = "Hi " + saved.getEmployee().getUsername() + ",\n\n" +
                "Your shift request (" + saved.getRequestedDays() + " / " + saved.getShift() + ") is " + status +
                "\n\nComment: " + (adminComment == null ? "" : adminComment);
        emailService.sendSimple(to, subj, body);

        return toDto(saved);
    }

    private ShiftRequestDto toDto(ShiftRequest r) {
        ShiftRequestDto d = new ShiftRequestDto();
        d.setId(r.getId());
        d.setUserId(r.getEmployee().getId());
        d.setRequestedDays(r.getRequestedDays());
        d.setShift(r.getShift());
        d.setStatus(r.getStatus());
        d.setAdminComment(r.getAdminComment());
        return d;
    }
}
