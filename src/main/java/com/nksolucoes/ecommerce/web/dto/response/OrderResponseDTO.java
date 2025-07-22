package com.nksolucoes.ecommerce.web.dto.response;

import com.nksolucoes.ecommerce.domain.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponseDTO(
        UUID id,
        Order.OrderStatus status,
        BigDecimal total,
        LocalDateTime createdAt,
        List<OrderItemResponseDTO> items
) {}

