package com.nksolucoes.ecommerce.web.dto;

import com.nksolucoes.ecommerce.domain.enumerations.RoleEnum;

import java.util.UUID;

public record UserDTO(UUID id, String email, RoleEnum role) {}
