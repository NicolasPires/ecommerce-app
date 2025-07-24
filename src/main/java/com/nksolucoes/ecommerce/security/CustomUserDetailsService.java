package com.nksolucoes.ecommerce.security;

import com.nksolucoes.ecommerce.domain.User;
import com.nksolucoes.ecommerce.infrastructure.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class CustomUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) {
        throw new UnsupportedOperationException("Should not be used directly, use JwtAuthFilter instead.");
    }
}
