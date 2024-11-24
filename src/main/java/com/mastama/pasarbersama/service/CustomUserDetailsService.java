package com.mastama.pasarbersama.service;

import com.mastama.pasarbersama.entity.Users;
import com.mastama.pasarbersama.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Cari user berdasarkan phoneNumber
        return usersRepository.findByPhoneNumber(username)
                .map(this::mapFromEntity) // Konversi entity Users ke UserDetails
                .orElseThrow(() -> new UsernameNotFoundException("User not found with phone number: " + username));
    }

    private UserDetails mapFromEntity(Users users) {
        return User.builder()
                .username(users.getPhoneNumber())
                .password(users.getPassword())
                .build();
    }
}
