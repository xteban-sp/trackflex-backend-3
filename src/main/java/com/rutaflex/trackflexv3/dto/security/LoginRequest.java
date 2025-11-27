package com.rutaflex.trackflexv3.dto.security;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
