package com.mastama.pasarbersama.controller;

import com.mastama.pasarbersama.dto.BaseResponse;
import com.mastama.pasarbersama.dto.response.RegisterResponse;
import com.mastama.pasarbersama.entity.request.RegisterRequest;
import com.mastama.pasarbersama.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UsersController {
    private final UsersService usersService;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<RegisterResponse>> register(@RequestBody RegisterRequest request) {
        log.info("Incoming Registering user");
        log.info("Outgoing registered user");
        return usersService.register(request);
    }

    @GetMapping
    public ResponseEntity<BaseResponse<RegisterResponse>> getUserByEmail(@RequestParam String email) {
        log.info("Incoming Getting user by email");
        log.info("Outgoing Getting user by email");
        return usersService.getUserByEmail(email);
    }
}
