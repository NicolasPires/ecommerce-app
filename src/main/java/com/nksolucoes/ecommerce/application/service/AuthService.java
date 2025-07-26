package com.nksolucoes.ecommerce.application.service;

import com.nksolucoes.ecommerce.domain.User;
import com.nksolucoes.ecommerce.infrastructure.repository.UserRepository;
import com.nksolucoes.ecommerce.security.JwtService;
import com.nksolucoes.ecommerce.web.dto.request.LoginRequestDTO;
import com.nksolucoes.ecommerce.web.dto.request.RegisterRequestDTO;
import com.nksolucoes.ecommerce.web.dto.response.AuthResponseDTO;
import com.nksolucoes.ecommerce.web.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper mapper;

    public AuthService(UserRepository repository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager,
                       UserMapper mapper) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.mapper = mapper;
    }

    public AuthResponseDTO register(RegisterRequestDTO request) {
        User user = mapper.fromRegisterRequest(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        User savedUser = repository.save(user);
        String token = jwtService.generateToken(savedUser);
        return new AuthResponseDTO(token);
    }

    public AuthResponseDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        User user = repository.findByEmail(request.email())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        String token = jwtService.generateToken(user);
        return new AuthResponseDTO(token);
    }
}
