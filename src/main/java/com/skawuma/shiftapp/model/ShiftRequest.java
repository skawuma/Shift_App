package com.skawuma.shiftapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * @author samuelkawuma
 * @package com.skawuma.shiftapp.model
 * @project Shift-App
 * @date 10/9/25
 */
@Entity
@Table(
        name = "shift_requests"
//        ,uniqueConstraints = @UniqueConstraint(columnNames = {"employee_id", "shift"})
)
@Getter
@Setter
@NoArgsConstructor
public class ShiftRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User employee;

    /**
     * Instead of days of week (like "Monday"), we now store
     * specific calendar dates (e.g. 2025-10-20) using LocalDate.
     * This allows the employee to select exact calendar days on the frontend.
     */
    @ElementCollection
    @CollectionTable(name = "shift_request_dates", joinColumns = @JoinColumn(name = "shift_request_id"))
    @Column(name = "requested_date")
    private List<LocalDate> requestedDates;

    private String shift; // 7am-3pm, 3pm-11pm, etc.

    private String status; // PENDING / APPROVED / REJECTED

    @Column(length = 2000)
    private String adminComment;
}
