package com.skawuma.shiftapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

/**
 * @author samuelkawuma
 * @package com.skawuma.shiftapp.model
 * @project Shift-App
 * @date 10/9/25
 */
@Entity
@Table(name = "shift_requests")
@Getter
@Setter
@NoArgsConstructor
public class ShiftRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User employee;

    @ElementCollection
    @CollectionTable(name="shift_request_days", joinColumns=@JoinColumn(name="shift_request_id"))
    @Column(name="day")
    private List<String> requestedDays;

    private String shift;

    private String status; // PENDING / APPROVED / REJECTED

    @Column(length = 2000)
    private String adminComment;

}
