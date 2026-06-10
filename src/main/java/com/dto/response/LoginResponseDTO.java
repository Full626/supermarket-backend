package com.dto.response;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String userId;
    private String userName;
    private String token;
    private String role;  // admin / cashier / manager
}