package com.nksolucoes.ecommerce.security;

import com.nksolucoes.ecommerce.domain.User;
import com.nksolucoes.ecommerce.domain.enumerations.RoleEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;

class CurrentUserResolverTest {

    private final CurrentUserResolver resolver = new CurrentUserResolver();

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldReturnUserFromSecurityContext() {
        User user = User.builder()
                .id(java.util.UUID.randomUUID())
                .email("user@example.com")
                .role(RoleEnum.CUSTOMER)
                .build();

        var auth = new UsernamePasswordAuthenticationToken(user, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        User resolved = resolver.resolveUserFromSecurityContext();

        assertThat(resolved).isNotNull();
        assertThat(resolved.getEmail()).isEqualTo("user@example.com");
    }

    @Test
    void shouldReturnNullIfAuthenticationIsMissing() {
        SecurityContextHolder.clearContext();

        User resolved = resolver.resolveUserFromSecurityContext();

        assertThat(resolved).isNull();
    }

    @Test
    void shouldReturnNullIfPrincipalIsNotUserInstance() {
        var auth = new UsernamePasswordAuthenticationToken("invalid", null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        User resolved = resolver.resolveUserFromSecurityContext();

        assertThat(resolved).isNull();
    }
}
