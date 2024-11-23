package com.mastama.pasarbersama.dto.response;

import lombok.Data;

@Data
public class RegisterResponse {
    private int id;
    private String name;
    private String email;
    private String phoneNumber;
}
