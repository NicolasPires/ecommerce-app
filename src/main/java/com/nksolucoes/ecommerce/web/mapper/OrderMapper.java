package com.nksolucoes.ecommerce.web.mapper;

import com.nksolucoes.ecommerce.domain.Order;
import com.nksolucoes.ecommerce.domain.OrderItem;
import com.nksolucoes.ecommerce.web.dto.response.OrderItemResponseDTO;
import com.nksolucoes.ecommerce.web.dto.response.OrderResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "total", target = "total")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "items", target = "items")
    OrderResponseDTO toResponse(Order order);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.price", target = "price")
    @Mapping(source = "quantity", target = "quantity")
    OrderItemResponseDTO toItemResponse(OrderItem item);
}

