package com.nksolucoes.ecommerce.application.service;

import com.nksolucoes.ecommerce.domain.User;
import com.nksolucoes.ecommerce.domain.enumerations.RoleEnum;
import com.nksolucoes.ecommerce.infrastructure.repository.UserRepository;
import com.nksolucoes.ecommerce.security.JwtService;
import com.nksolucoes.ecommerce.web.dto.request.LoginRequestDTO;
import com.nksolucoes.ecommerce.web.dto.request.RegisterRequestDTO;
import com.nksolucoes.ecommerce.web.dto.response.AuthResponseDTO;
import com.nksolucoes.ecommerce.web.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock private UserRepository repository;
    @Mock private JwtService jwtService;
    @Mock private PasswordEncoder encoder;
    @Mock private AuthenticationManager authManager;
    @Mock private UserMapper mapper;

    @Test
    void shouldRegisterUserSuccessfully() {
        RegisterRequestDTO dto = new RegisterRequestDTO("User", "user@email.com", "123", "CLIENTE");
        User user = User.builder().name("User").email("user@email.com").password("hashed").role(RoleEnum.CUSTOMER).build();
        when(encoder.encode("123")).thenReturn("hashed");
        when(mapper.fromRegisterRequest(dto)).thenReturn(user);
        when(repository.save(user)).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("jwt-token");

        AuthResponseDTO response = authService.register(dto);

        assertThat(response.token()).isEqualTo("jwt-token");
        verify(repository).save(user);
    }

    @Test
    void shouldLoginSuccessfully() {
        LoginRequestDTO dto = new LoginRequestDTO("user@email.com", "123");
        User user = User.builder().email("user@email.com").password("123").role(RoleEnum.CUSTOMER).build();

        when(repository.findByEmail("user@email.com")).thenReturn(java.util.Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwt-token");

        AuthResponseDTO response = authService.login(dto);

        assertThat(response.token()).isEqualTo("jwt-token");
        verify(authManager).authenticate(new UsernamePasswordAuthenticationToken("user@email.com", "123"));
    }
}
