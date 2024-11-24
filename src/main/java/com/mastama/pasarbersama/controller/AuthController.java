package com.mastama.pasarbersama.controller;

import com.mastama.pasarbersama.dto.BaseResponse;
import com.mastama.pasarbersama.dto.response.RegisterResponse;
import com.mastama.pasarbersama.entity.request.LoginRequest;
import com.mastama.pasarbersama.entity.request.RegisterRequest;
import com.mastama.pasarbersama.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<RegisterResponse>> register(@RequestBody RegisterRequest request) {
        log.info("Incoming Register request by phone number: {}", request.getPhoneNumber());
        return authService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<String>> login(@RequestBody LoginRequest request) {
        log.info("Incoming login: {}", request.getPhoneNumber());
        BaseResponse<String> response = authService.login(request).getBody();
        log.info("Outgoing Response: {}", request.getPhoneNumber());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
