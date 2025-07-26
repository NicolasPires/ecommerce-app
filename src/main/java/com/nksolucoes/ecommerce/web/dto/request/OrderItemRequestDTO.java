package com.nksolucoes.ecommerce.web.dto.request;

import java.util.UUID;

public record OrderItemRequestDTO(
        UUID productId,
        int quantity
) {}
