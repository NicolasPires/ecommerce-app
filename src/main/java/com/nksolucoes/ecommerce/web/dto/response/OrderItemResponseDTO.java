package com.nksolucoes.ecommerce.web.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponseDTO(
        UUID productId,
        String productName,
        BigDecimal price,
        int quantity
) {}

