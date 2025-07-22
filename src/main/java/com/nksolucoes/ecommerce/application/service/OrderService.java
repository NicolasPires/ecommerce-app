package com.nksolucoes.ecommerce.application.service;


import com.nksolucoes.ecommerce.domain.*;
import com.nksolucoes.ecommerce.infrastructure.repository.OrderRepository;
import com.nksolucoes.ecommerce.infrastructure.repository.ProductRepository;
import com.nksolucoes.ecommerce.web.dto.request.OrderRequestDTO;
import com.nksolucoes.ecommerce.web.dto.response.OrderResponseDTO;
import com.nksolucoes.ecommerce.web.mapper.OrderMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderMapper mapper;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, OrderMapper mapper) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.mapper = mapper;
    }

    @Transactional
    public OrderResponseDTO createOrder(User user, OrderRequestDTO dto) {
        Order order = new Order();
        order.setUser(user);

        List<OrderItem> items = dto.items().stream().map(itemDTO -> {
            Product product = productRepository.findById(itemDTO.productId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));

            if (product.getStockQuantity() < itemDTO.quantity()) {
                order.setStatus(Order.OrderStatus.CANCELLED);
                throw new IllegalStateException("Insufficient stock for product: " + product.getName());
            }

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemDTO.quantity());

            return item;
        }).toList();

        order.setItems(items);

        Order saved = orderRepository.save(order);
        return mapper.toResponse(saved);
    }

    @Transactional
    public OrderResponseDTO payOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (order.getStatus() != Order.OrderStatus.PENDING) {
            throw new IllegalStateException("Order already processed.");
        }

        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            if (product.getStockQuantity() < item.getQuantity()) {
                order.setStatus(Order.OrderStatus.CANCELLED);
                orderRepository.save(order);
                throw new IllegalStateException("Insufficient stock for product: " + product.getName());
            }
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);
        }

        order.setStatus(Order.OrderStatus.PAID);
        orderRepository.save(order);

        return mapper.toResponse(order);
    }

    public List<OrderResponseDTO> listUserOrders(User user) {
        return orderRepository.findByUser(user).stream()
                .map(mapper::toResponse)
                .toList();
    }
}

