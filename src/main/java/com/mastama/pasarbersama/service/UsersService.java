package com.mastama.pasarbersama.service;

import com.mastama.pasarbersama.dto.BaseResponse;
import com.mastama.pasarbersama.entity.Users;
import com.mastama.pasarbersama.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;

    /*
    cannot to use because one account has one token match
     */
//    public ResponseEntity<BaseResponse<RegisterResponse>> getUserByPhoneNumber(String phoneNumber) {
//        log.info("Start get user by email or phone number");
//        BaseResponse<RegisterResponse> response = new BaseResponse<>();
//        try {
//            Optional<Users> usersOptional = usersRepository.findByPhoneNumber(phoneNumber);
//            //check by email or by phoneNumber
//            if (usersOptional.isEmpty()) {
//                response.setResponseStatus(false);
//                response.setResponseDesc("User not found");
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//            }
//
//            // Jika user ditemukan, ambil data dari Optional
//            Users user = usersOptional.get();
//
//            //convert Users ke DTO registerResponse
//            RegisterResponse registerResponse = new RegisterResponse();
//            registerResponse.setId(user.getId());
//            registerResponse.setName(user.getName());
//            registerResponse.setEmail(user.getEmail());
//            registerResponse.setPhoneNumber(user.getPhoneNumber());
//
//            response.setResponseStatus(true);
//            response.setResponseDesc("Get User registered successfully");
//            response.setData(registerResponse);
//            log.info("Successfully fetched user");
//        } catch (Exception e) {
//            log.error("Error while getting user by email", e);
//            response.setResponseStatus(false);
//            response.setResponseDesc(e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//        log.info("End get user by email or phone number");
//        return ResponseEntity.status(HttpStatus.OK).body(response);
//    }

    public ResponseEntity<BaseResponse<Users>> users() {
        log.info("Start get user by token authorization");
        BaseResponse<Users> response = new BaseResponse<>();

        // Ambil user details dari SecurityContext
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        // Ambil phoneNumber dari userDetails
        String phoneNumber = userDetails.getUsername();

        // Cari user berdasarkan phone number
        Users users = usersRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("User with the phone number not found"));

        response.setResponseStatus(true);
        response.setResponseDesc("User with the phone number " + users.getPhoneNumber());
        response.setData(users);

        log.info("End get user by token authorization");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
