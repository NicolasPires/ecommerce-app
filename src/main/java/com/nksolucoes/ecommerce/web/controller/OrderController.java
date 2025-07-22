package com.nksolucoes.ecommerce.web.controller;

import com.nksolucoes.ecommerce.application.service.OrderService;
import com.nksolucoes.ecommerce.domain.User;
import com.nksolucoes.ecommerce.security.CurrentUser;
import com.nksolucoes.ecommerce.web.dto.request.OrderRequestDTO;
import com.nksolucoes.ecommerce.web.dto.response.OrderResponseDTO;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    @RolesAllowed("USER")
    public ResponseEntity<OrderResponseDTO> create(@RequestBody OrderRequestDTO dto,
                                                   @CurrentUser User user) {
        return ResponseEntity.ok(service.createOrder(user, dto));
    }

    @PostMapping("/{id}/pay")
    @RolesAllowed("USER")
    public ResponseEntity<OrderResponseDTO> pay(@PathVariable UUID id) {
        return ResponseEntity.ok(service.payOrder(id));
    }

    @GetMapping
    @RolesAllowed("USER")
    public ResponseEntity<List<OrderResponseDTO>> list(@CurrentUser User user) {
        return ResponseEntity.ok(service.listUserOrders(user));
    }
}
