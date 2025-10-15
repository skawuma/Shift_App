package com.skawuma.shiftapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author samuelkawuma
 * @package com.skawuma.shiftapp.dto
 * @project Shift-App
 * @date 10/12/25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String role;
    private Long userId;
    private String email;
    private String username;

//    public AuthResponse() {}
//
//    public AuthResponse(String token, String role, Long userId, String email) {
//        this.token = token;
//        this.role = role;
//        this.userId = userId;
//        this.email = email;
//    }
//
//    public String getToken() { return token; }
//    public String getRole() { return role; }
//    public Long getUserId() { return userId; }
//    public String getEmail() { return email; }
}
