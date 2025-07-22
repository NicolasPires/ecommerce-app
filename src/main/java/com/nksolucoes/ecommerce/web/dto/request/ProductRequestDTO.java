package com.nksolucoes.ecommerce.web.dto.request;

import java.math.BigDecimal;

public record ProductRequestDTO(
        String name,
        String description,
        BigDecimal price,
        String category,
        Integer stockQuantity
) {}

