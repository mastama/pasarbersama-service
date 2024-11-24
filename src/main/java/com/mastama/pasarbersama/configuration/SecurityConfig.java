package com.mastama.pasarbersama.configuration;

import com.mastama.pasarbersama.component.JwtAuthFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    private static final String[] ALLOWED = {
            "/api/v1/auth/login",
            "/api/v1/auth/logout",
            "/api/v1/auth/register",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                // Konfigurasi endpoint yang diperbolehkan tanpa autentikasi
                .authorizeHttpRequests(registry -> registry
                        .requestMatchers(ALLOWED) // `ALLOWED` adalah daftar endpoint bebas autentikasi
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                // Konfigurasi session management (stateless karena menggunakan JWT)
                .sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // Konfigurasi exception handling
                .exceptionHandling(exceptionHandlingConfigurer -> exceptionHandlingConfigurer
                        // Custom handler untuk akses yang tidak diotorisasi
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write(
                                    "{\"error\":\"Unauthorized\",\"message\":\"Authentication required\"}"
                            );
                        })
                        // Custom handler untuk akses yang dilarang (forbidden) || jika ada role user
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json");
                            response.getWriter().write(
                                    "{\"error\":\"Forbidden\",\"message\":\"Access is denied\"}"
                            );
                        })
                )
                // Menambahkan filter JWT sebelum filter UsernamePasswordAuthentication
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
