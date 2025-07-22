package com.nksolucoes.ecommerce.web.dto;

import com.nksolucoes.ecommerce.domain.User.Role;

import java.util.UUID;

public record UserDTO(UUID id, String email, Role role) {}
