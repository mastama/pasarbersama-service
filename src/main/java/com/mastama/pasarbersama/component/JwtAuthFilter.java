package com.mastama.pasarbersama.component;

import com.mastama.pasarbersama.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Jika permintaan untuk endpoint auth, lanjutkan tanpa filter
        if (request.getServletPath().contains("/api/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");

        // Periksa apakah authHeader null atau tidak dimulai dengan "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String token = authHeader.substring(7).trim(); // Menghapus "Bearer " untuk mendapatkan token
            final String phoneNumber = jwtUtils.extractClaims(token, Claims::getSubject);

            // Validasi phoneNumber dan cek apakah sudah ada authentication
            if (phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                final UserDetails userDetails = customUserDetailsService.loadUserByUsername(phoneNumber);

                // Validasi token
                if (jwtUtils.isTokenValid(token, userDetails)) {
                    // Buat authentication baru dan set ke SecurityContext
                    final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );

                    authentication.setDetails(new WebAuthenticationDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            // Handle error (misalnya token tidak valid atau parsing error)
            log.error("Authentication error: {}", e.getMessage());
            SecurityContextHolder.clearContext(); // Bersihkan konteks jika ada error
        }

        // Lanjutkan ke filter berikutnya
        filterChain.doFilter(request, response);
    }

}
