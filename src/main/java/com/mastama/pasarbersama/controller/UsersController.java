package com.mastama.pasarbersama.controller;

import com.mastama.pasarbersama.dto.BaseResponse;
import com.mastama.pasarbersama.entity.Users;
import com.mastama.pasarbersama.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UsersController {
    private final UsersService usersService;

//    @GetMapping
//    public ResponseEntity<BaseResponse<RegisterResponse>> getUserByPhoneNumber(@RequestParam String phoneNumber) {
//        log.info("Incoming Getting user by phone number: {}", phoneNumber);
//        log.info("Outgoing Getting user by phone number: {}", phoneNumber);
//        return usersService.getUserByPhoneNumber(phoneNumber);
//    }

    @GetMapping
    public ResponseEntity<BaseResponse<Users>> getUser() {
        log.info("Incoming get user");
        BaseResponse<Users> response = usersService.users().getBody();
        log.info("Outgoing get user");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
