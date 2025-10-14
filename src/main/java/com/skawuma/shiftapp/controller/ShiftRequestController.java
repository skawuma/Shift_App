package com.skawuma.shiftapp.controller;

import com.skawuma.shiftapp.dto.ShiftRequestDto;
import com.skawuma.shiftapp.dto.UpdateStatusDto;
import com.skawuma.shiftapp.model.ShiftRequest;
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
@CrossOrigin(origins = "http://localhost:4200") // allow Angular dev server
public class ShiftRequestController {
    private final ShiftRequestService service;

    public ShiftRequestController(ShiftRequestService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ShiftRequestDto> submit(@RequestBody ShiftRequestDto dto) {
        return ResponseEntity.ok(service.submit(dto));
    }

    @GetMapping("/admin")
    public ResponseEntity<Page<ShiftRequestDto>> listAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(service.listAll(page, size, status));
    }

    @GetMapping("/user")
    public ResponseEntity<List<ShiftRequestDto>> listByUser(@RequestParam Long userId) {
        return ResponseEntity.ok(service.listByUser(userId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ShiftRequestDto> updateStatus(@PathVariable Long id, @RequestBody ShiftRequestDto dto) {
        return ResponseEntity.ok(service.updateStatus(id, dto.getStatus(), dto.getAdminComment()));
    }

    // convenience separate endpoints
    @PutMapping("/{id}/approve")
    public ResponseEntity<ShiftRequestDto> approve(@PathVariable Long id, @RequestBody(required=false) ShiftRequestDto dto) {
        return ResponseEntity.ok(service.updateStatus(id, "APPROVED", dto == null ? null : dto.getAdminComment()));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<ShiftRequestDto> reject(@PathVariable Long id, @RequestBody(required=false) ShiftRequestDto dto) {
        return ResponseEntity.ok(service.updateStatus(id, "REJECTED", dto == null ? null : dto.getAdminComment()));
    }
}
