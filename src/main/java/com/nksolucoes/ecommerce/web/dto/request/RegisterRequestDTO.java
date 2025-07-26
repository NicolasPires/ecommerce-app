package com.nksolucoes.ecommerce.web.dto.request;

public record RegisterRequestDTO(
        String name,
        String email,
        String password,
        String role
) {}
