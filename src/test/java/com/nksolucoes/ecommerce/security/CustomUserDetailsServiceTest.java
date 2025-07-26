package com.nksolucoes.ecommerce.security;

import com.nksolucoes.ecommerce.domain.User;
import com.nksolucoes.ecommerce.domain.enumerations.RoleEnum;
import com.nksolucoes.ecommerce.infrastructure.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    private UserRepository repository;
    private CustomUserDetailsService service;

    @BeforeEach
    void setUp() {
        repository = mock(UserRepository.class);
        service = new CustomUserDetailsService(repository);
    }

    @Test
    void shouldLoadUserByUsername() {
        User user = User.builder()
                .email("user@test.com")
                .password("pwd")
                .role(RoleEnum.CUSTOMER)
                .build();

        when(repository.findByEmail("user@test.com")).thenReturn(Optional.of(user));

        var result = service.loadUserByUsername("user@test.com");

        assertThat(result).isInstanceOf(CustomUserDetails.class);
        assertThat(result.getUsername()).isEqualTo("user@test.com");
        assertThat(result.getAuthorities()).extracting("authority")
                .containsExactly("ROLE_CUSTOMER");
    }

    @Test
    void shouldThrowIfUserNotFound() {
        when(repository.findByEmail("user@test.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.loadUserByUsername("user@test.com"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User not found");
    }
}
