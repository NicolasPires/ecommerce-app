package com.nksolucoes.ecommerce.web.dto.response;

import com.nksolucoes.ecommerce.domain.enumerations.OrderStatusEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponseDTO(
        UUID id,
        OrderStatusEnum status,
        BigDecimal total,
        LocalDateTime createdAt,
        List<OrderItemResponseDTO> items
) {}

