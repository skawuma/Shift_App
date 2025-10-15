package com.skawuma.shiftapp.service;

import com.skawuma.shiftapp.dto.ShiftRequestDto;
import com.skawuma.shiftapp.model.ShiftRequest;
import com.skawuma.shiftapp.model.User;
import com.skawuma.shiftapp.repository.ShiftRequestRepository;
import com.skawuma.shiftapp.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
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

    // ===========================
    // SUBMIT SHIFT REQUEST
    // ===========================
    public ShiftRequestDto submit(ShiftRequestDto dto) {
        User u = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate to prevent duplicate requests for same date & shift
        for (LocalDate date : dto.getRequestedDates()) {
            boolean exists = repo.existsByEmployeeAndRequestedDatesContainsAndShift(u, date, dto.getShift());
            if (exists) {
                throw new RuntimeException("Duplicate request already exists for " + date + " and shift " + dto.getShift());
            }
        }

        ShiftRequest sr = new ShiftRequest();
        sr.setEmployee(u);
        sr.setRequestedDates(dto.getRequestedDates());
        sr.setShift(dto.getShift());
        sr.setStatus("PENDING");

        ShiftRequest saved = repo.save(sr);
        return toDto(saved);
    }

    // ===========================
    // PAGINATED LISTING
    // ===========================
    public Page<ShiftRequestDto> listAll(int page, int size, String status) {
        Pageable p = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<ShiftRequest> pageEnt = (status == null || status.isBlank())
                ? repo.findAll(p)
                : repo.findByStatus(status, p);

        return pageEnt.map(this::toDto);
    }

    // ===========================
    // USER-SPECIFIC LIST
    // ===========================
    public List<ShiftRequestDto> listByUser(Long userId) {
        return repo.findAll().stream()
                .filter(r -> r.getEmployee().getId().equals(userId))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ===========================
    // ADMIN UPDATES STATUS
    // ===========================
    public ShiftRequestDto updateStatus(Long id, String status, String adminComment) {
        ShiftRequest r = repo.findById(id).orElseThrow(() -> new RuntimeException("Request not found"));
        r.setStatus(status);
        r.setAdminComment(adminComment);
        ShiftRequest saved = repo.save(r);

        // Send email notification
        String to = saved.getEmployee().getEmail();
        String subj = "Shift Request " + saved.getId() + " - " + status;
        String body = "Hi " + saved.getEmployee().getUsername() + ",\n\n" +
                "Your shift request (" + saved.getRequestedDates() + " / " + saved.getShift() + ") is " + status +
                "\n\nComment: " + (adminComment == null ? "" : adminComment);


        // Send email notification with html

        String htmlBody = """
    <div style="font-family:Arial,sans-serif;line-height:1.6;color:#333;">
      <h2 style="color:#1976d2;">Shift Request Update</h2>
      <p>Hi <b>%s</b>,</p>
      <p>Your shift request for <b>%s</b> (%s) has been 
      <span style="font-weight:bold;color:%s;">%s</span>.</p>
      %s
      <br><br>
      <p>Thank you,<br/>Shift Management Team</p>
    </div>
    """.formatted(
                saved.getEmployee().getUsername(),
                saved.getRequestedDates(),
                saved.getShift(),
                "APPROVED".equalsIgnoreCase(status) ? "green" : "red",
                status,
                adminComment != null && !adminComment.isBlank()
                        ? "<p><i>Admin comment:</i> " + adminComment + "</p>"
                        : ""
        );

       // emailService.sendSimple(to, subj, body);
        emailService.sendHtml(to, subj, htmlBody);

        return toDto(saved);
    }

    // ===========================
    // DTO MAPPER
    // ===========================
    private ShiftRequestDto toDto(ShiftRequest r) {
        ShiftRequestDto d = new ShiftRequestDto();
        d.setId(r.getId());
        d.setUserId(r.getEmployee().getId());
        d.setRequestedDates(r.getRequestedDates());
        d.setShift(r.getShift());
        d.setStatus(r.getStatus());
        d.setAdminComment(r.getAdminComment());
        return d;
    }
}
