package com.mastama.pasarbersama.service;

import com.mastama.pasarbersama.dto.BaseResponse;
import com.mastama.pasarbersama.dto.response.RegisterResponse;
import com.mastama.pasarbersama.entity.Users;
import com.mastama.pasarbersama.entity.request.RegisterRequest;
import com.mastama.pasarbersama.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;

    public ResponseEntity<BaseResponse<RegisterResponse>> register(RegisterRequest request) {
        log.info("Start register user");
        BaseResponse<RegisterResponse> response = new BaseResponse<>();

        // check email exist or not
        if (usersRepository.existsByEmail(request.getEmail())) {
            response.setResponseStatus(false);
            response.setResponseDesc("Email already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        // check phoneNumber exist or not
        if (usersRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            response.setResponseStatus(false);
            response.setResponseDesc("Phone number already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        try {
            // create and save user
            Users user = new Users();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPhoneNumber(request.getPhoneNumber());
            user.setPassword(request.getPassword());

            // save to db
            Users savedUser = usersRepository.save(user);

            // set response
            RegisterResponse registerResponse = new RegisterResponse();
            registerResponse.setId(savedUser.getId());
            registerResponse.setName(savedUser.getName());
            registerResponse.setEmail(savedUser.getEmail());
            registerResponse.setPhoneNumber(savedUser.getPhoneNumber());

            response.setResponseStatus(true);
            response.setResponseDesc("User registered successfully");
            response.setData(registerResponse);
        } catch (Exception e) {
            log.error("Error while registering user", e);
            response.setResponseStatus(false);
            response.setResponseDesc(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        log.info("End register user");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<BaseResponse<RegisterResponse>> getUserByEmail(String email) {
        log.info("Start get user by email");
        BaseResponse<RegisterResponse> response = new BaseResponse<>();

        //check exist email
        Users users = usersRepository.findByEmail(email);
        if (users == null) {
            response.setResponseStatus(false);
            response.setResponseDesc("User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        try {
            //convert Users ke DTO registerResponse
            RegisterResponse registerResponse = new RegisterResponse();
            registerResponse.setId(users.getId());
            registerResponse.setName(users.getName());
            registerResponse.setEmail(users.getEmail());
            registerResponse.setPhoneNumber(users.getPhoneNumber());

            response.setResponseStatus(true);
            response.setResponseDesc("Get User registered successfully");
            response.setData(registerResponse);
        } catch (Exception e) {
            log.error("Error while getting user by email", e);
            response.setResponseStatus(false);
            response.setResponseDesc(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
