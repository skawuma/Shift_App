package com.skawuma.shiftapp.dto;

import com.skawuma.shiftapp.model.RequestStatus;

/**
 * @author samuelkawuma
 * @package com.skawuma.shiftapp.dto
 * @project Shift-App
 * @date 10/9/25
 */
public class UpdateStatusDto {

    private RequestStatus status;
    private String adminComment;
    // getters/setters
    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }
    public String getAdminComment() { return adminComment; }
    public void setAdminComment(String adminComment) { this.adminComment = adminComment; }

}
