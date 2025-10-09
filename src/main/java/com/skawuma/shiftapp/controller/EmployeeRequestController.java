package com.skawuma.shiftapp.controller;

import com.skawuma.shiftapp.dto.CreateRequestDto;
import com.skawuma.shiftapp.dto.UpdateStatusDto;
import com.skawuma.shiftapp.model.EmployeeRequest;
import com.skawuma.shiftapp.service.EmployeeRequestService;
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
public class EmployeeRequestController {
    private final EmployeeRequestService service;
    public EmployeeRequestController(EmployeeRequestService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<EmployeeRequest> create(@RequestBody CreateRequestDto dto) {
        EmployeeRequest created = service.create(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeRequest>> list() {
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<EmployeeRequest> updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateStatusDto dto) {
        EmployeeRequest updated = service.updateStatus(id, dto.getStatus(), dto.getAdminComment());
        return ResponseEntity.ok(updated);
    }
}
