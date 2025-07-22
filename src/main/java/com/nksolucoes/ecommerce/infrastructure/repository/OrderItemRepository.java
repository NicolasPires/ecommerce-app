package com.nksolucoes.ecommerce.infrastructure.repository;

import com.nksolucoes.ecommerce.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
}
