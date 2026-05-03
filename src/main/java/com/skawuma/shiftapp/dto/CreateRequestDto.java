package com.skawuma.shiftapp.dto;

import com.skawuma.shiftapp.model.Shift;

import java.time.DayOfWeek;
import java.util.Set;

/**
 * @author samuelkawuma
 * @package com.skawuma.shiftapp.dto
 * @project Shift-App
 * @date 10/9/25
 */
public class CreateRequestDto {
    private String employeeName;
    private Set<DayOfWeek> requestedDays;
    private Shift shift;
    // getters/setters
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public Set<DayOfWeek> getRequestedDays() { return requestedDays; }
    public void setRequestedDays(Set<DayOfWeek> requestedDays) { this.requestedDays = requestedDays; }
    public Shift getShift() { return shift; }
    public void setShift(Shift shift) { this.shift = shift; }
}
