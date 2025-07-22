package com.nksolucoes.ecommerce.web.mapper;

import com.nksolucoes.ecommerce.domain.Order;
import com.nksolucoes.ecommerce.domain.OrderItem;
import com.nksolucoes.ecommerce.web.dto.response.OrderItemResponseDTO;
import com.nksolucoes.ecommerce.web.dto.response.OrderResponseDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponseDTO toResponse(Order order);
    List<OrderItemResponseDTO> toItemResponses(List<OrderItem> items);
}

