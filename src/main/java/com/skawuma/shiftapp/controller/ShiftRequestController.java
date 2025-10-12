package com.skawuma.shiftapp.controller;

import com.skawuma.shiftapp.dto.ShiftRequestDto;
import com.skawuma.shiftapp.dto.UpdateStatusDto;
import com.skawuma.shiftapp.model.ShiftRequest;
import com.skawuma.shiftapp.service.ShiftRequestService;
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
    public ShiftRequestController(ShiftRequestService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<ShiftRequest> create(@RequestBody ShiftRequestDto dto) {
        ShiftRequest created = service.create(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<ShiftRequest>> list() {
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ShiftRequest> updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateStatusDto dto) {
        ShiftRequest updated = service.updateStatus(id, dto.getStatus(), dto.getAdminComment());
        return ResponseEntity.ok(updated);
    }
}
