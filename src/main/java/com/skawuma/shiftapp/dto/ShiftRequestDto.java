package com.skawuma.shiftapp.dto;

import com.skawuma.shiftapp.model.Shift;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

/**
 * @author samuelkawuma
 * @package com.skawuma.shiftapp.dto
 * @project Shift-App
 * @date 10/9/25
 */
public class ShiftRequestDto {
    private Long id;
    private Long userId; // employee id
    private List<String> requestedDays;
    private String shift;
    private String status;
    private String adminComment;

    public ShiftRequestDto() {}

    // getters / setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public List<String> getRequestedDays() { return requestedDays; }
    public void setRequestedDays(List<String> requestedDays) { this.requestedDays = requestedDays; }

    public String getShift() { return shift; }
    public void setShift(String shift) { this.shift = shift; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAdminComment() { return adminComment; }
    public void setAdminComment(String adminComment) { this.adminComment = adminComment; }
}
