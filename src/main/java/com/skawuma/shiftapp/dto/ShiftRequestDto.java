package com.skawuma.shiftapp.dto;

import com.skawuma.shiftapp.model.Shift;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * @author samuelkawuma
 * @package com.skawuma.shiftapp.dto
 * @project Shift-App
 * @date 10/9/25
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShiftRequestDto {
    private Long id;
    private Long userId; // employee id
    private Long employeeId;
    private List<LocalDate> requestedDates;
    private String shift;
    private String status;
    private String adminComment;

}
