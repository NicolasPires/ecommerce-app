package com.nksolucoes.ecommerce.infrastructure.repository;

import com.nksolucoes.ecommerce.domain.Order;
import com.nksolucoes.ecommerce.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByUser(User user);
}

