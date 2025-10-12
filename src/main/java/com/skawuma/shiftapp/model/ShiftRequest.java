package com.skawuma.shiftapp.model;

import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.util.Set;

/**
 * @author samuelkawuma
 * @package com.skawuma.shiftapp.model
 * @project Shift-App
 * @date 10/9/25
 */
@Entity
@Table(name = "employee_requests")
public class ShiftRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String employeeName; // could be employee id/email in real app

    @ElementCollection(targetClass = DayOfWeek.class)
    @CollectionTable(name = "request_days", joinColumns = @JoinColumn(name = "request_id"))
    @Column(name = "day")
    @Enumerated(EnumType.STRING)
    private Set<DayOfWeek> requestedDays;

    @Enumerated(EnumType.STRING)
    private Shift shift;

    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING;

    private String adminComment;

    // constructors, getters, setters
    public ShiftRequest() {}

    // getters and setters omitted for brevity — generate them or use Lombok
    // ... (include all getters/setters)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public Set<DayOfWeek> getRequestedDays() { return requestedDays; }
    public void setRequestedDays(Set<DayOfWeek> requestedDays) { this.requestedDays = requestedDays; }
    public Shift getShift() { return shift; }
    public void setShift(Shift shift) { this.shift = shift; }
    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }
    public String getAdminComment() { return adminComment; }
    public void setAdminComment(String adminComment) { this.adminComment = adminComment; }

}
