package com.mastama.pasarbersama.service;

import com.mastama.pasarbersama.component.JwtUtils;
import com.mastama.pasarbersama.dto.BaseResponse;
import com.mastama.pasarbersama.dto.response.RegisterResponse;
import com.mastama.pasarbersama.entity.Users;
import com.mastama.pasarbersama.entity.request.LoginRequest;
import com.mastama.pasarbersama.entity.request.RegisterRequest;
import com.mastama.pasarbersama.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public ResponseEntity<BaseResponse<RegisterResponse>> register(RegisterRequest request) {
        log.info("Start register user with jwt");
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
            user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));

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
            log.error("Error while registering user: {}", e.getMessage());
            response.setResponseStatus(false);
            response.setResponseDesc(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        log.info("End register user with jwt");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<BaseResponse<String>> login(LoginRequest request) {
        log.info("Start login user with jwt: {}", request.getPhoneNumber());
        BaseResponse<String> response = new BaseResponse<>();

        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    request.getPhoneNumber(), request.getPassword()
            );

            Users users = usersRepository.findByPhoneNumber(request.getPhoneNumber())
                    .orElseThrow(() -> new UsernameNotFoundException("User with phone number not found"));

            //validasi password
            if (!bCryptPasswordEncoder.matches(request.getPassword(), users.getPassword())) {
                response.setResponseStatus(false);
                response.setResponseDesc("Wrong password");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // untuk memastikan request benar
            authenticationManager.authenticate(authenticationToken);

            //generate token
            String token = jwtUtils.generateToken(users);

            response.setResponseStatus(true);
            response.setResponseDesc("User logged in successfully");
            response.setData(token);

        } catch (Exception e) {
            response.setResponseStatus(false);
            response.setResponseDesc("an error occured: {}" + e.getMessage());
        }

        log.info("End login user with jwt: {}", request.getPhoneNumber());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
