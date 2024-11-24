package com.mastama.pasarbersama.entity.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Password is required")
    private String password;
    @NotBlank(message = "PhoneNumber is required")
    private String phoneNumber;
}
