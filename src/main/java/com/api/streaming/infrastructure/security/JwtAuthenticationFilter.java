package com.api.streaming.infrastructure.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.api.streaming.infrastructure.persistence.implementations.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7);
            String username = tokenProvider.extractSubject(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                userRepository.findUserByUsername(username).ifPresent(user -> {
                    if (tokenProvider.isTokenValid(token, user)) {
                        var authToken = new UsernamePasswordAuthenticationToken(
                                user, null, List.of(new SimpleGrantedAuthority(user.getRole().name())));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                });
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        filterChain.doFilter(request, response);
    }
    
}
