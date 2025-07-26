package com.nksolucoes.ecommerce.security;

import com.nksolucoes.ecommerce.domain.User;
import com.nksolucoes.ecommerce.domain.enumerations.RoleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setup() {
        // Chave secreta simulada (tem que ter pelo menos 32 bytes para HS256)
        String fakeSecret = "mysecretkeyformytestmysecretkeyformytest";
        long fakeExpiration = 1000 * 60 * 10; // 10 minutos

        jwtService = new JwtService(fakeSecret, fakeExpiration);
    }

    @Test
    void shouldGenerateAndValidateToken() {
        User user = User.builder()
                .email("user@test.com")
                .role(RoleEnum.CUSTOMER)
                .build();

        String token = jwtService.generateToken(user);
        String extractedEmail = jwtService.extractUsername(token);

        assertThat(token).isNotBlank();
        assertThat(extractedEmail).isEqualTo(user.getEmail());
    }
}
