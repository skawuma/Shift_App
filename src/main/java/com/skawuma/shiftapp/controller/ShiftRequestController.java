package com.skawuma.shiftapp.controller;

import com.skawuma.shiftapp.dto.ShiftRequestDto;
import com.skawuma.shiftapp.service.ShiftRequestService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author samuelkawuma
 * @package com.skawuma.shiftapp.controller
 * @project Shift-App
 * @date 10/9/25
 */

@RestController
@RequestMapping("/api/requests")
public class ShiftRequestController {

    private final ShiftRequestService service;

    public ShiftRequestController(ShiftRequestService service) {
        this.service = service;
    }

    /**
     * Employee submits a shift request with specific calendar dates.
     * Example JSON:
     * {
     *   "userId": 1,
     *   "requestedDates": ["2025-10-20", "2025-10-21"],
     *   "shift": "7am-3pm"
     * }
     */
    @PostMapping
    public ResponseEntity<ShiftRequestDto> submit(@RequestBody ShiftRequestDto dto) {
        return ResponseEntity.ok(service.submit(dto));
    }

    /**
     * Admin fetches paginated shift requests, optionally filtered by status.
     */
    @GetMapping("/admin")
    public ResponseEntity<Page<ShiftRequestDto>> listAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(service.listAll(page, size, status));
    }

    /**
     * Employee fetches their own shift requests by userId.
     */
    @GetMapping("/user")
    public ResponseEntity<List<ShiftRequestDto>> listByUser(@RequestParam Long userId) {
        return ResponseEntity.ok(service.listByUser(userId));
    }

    /**
     * Admin manually updates request status (custom comment allowed).
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<ShiftRequestDto> updateStatus(
            @PathVariable Long id,
            @RequestBody ShiftRequestDto dto) {
        return ResponseEntity.ok(service.updateStatus(id, dto.getStatus(), dto.getAdminComment()));
    }

    /**
     * Convenience endpoint for approval (auto sets status = APPROVED)
     */
    @PutMapping("/{id}/approve")
    public ResponseEntity<ShiftRequestDto> approve(
            @PathVariable Long id,
            @RequestBody(required = false) ShiftRequestDto dto) {
        return ResponseEntity.ok(service.updateStatus(id, "APPROVED",
                dto == null ? null : dto.getAdminComment()));
    }

    /**
     * Convenience endpoint for rejection (auto sets status = REJECTED)
     */
    @PutMapping("/{id}/reject")
    public ResponseEntity<ShiftRequestDto> reject(
            @PathVariable Long id,
            @RequestBody(required = false) ShiftRequestDto dto) {
        return ResponseEntity.ok(service.updateStatus(id, "REJECTED",
                dto == null ? null : dto.getAdminComment()));
    }
}
