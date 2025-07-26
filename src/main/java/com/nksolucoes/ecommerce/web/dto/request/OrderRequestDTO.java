package com.nksolucoes.ecommerce.web.dto.request;


import com.nksolucoes.ecommerce.web.dto.OrderItemDTO;

import java.util.List;

public record OrderRequestDTO(List<OrderItemRequestDTO> items) {}

