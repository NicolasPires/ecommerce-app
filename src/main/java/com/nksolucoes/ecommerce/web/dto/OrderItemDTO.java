package com.nksolucoes.ecommerce.web.dto;

import java.util.UUID;

public record OrderItemDTO(UUID productId, int quantity) {}
