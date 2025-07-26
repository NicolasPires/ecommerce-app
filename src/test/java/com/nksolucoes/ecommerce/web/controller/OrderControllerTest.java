package com.nksolucoes.ecommerce.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nksolucoes.ecommerce.application.service.OrderService;
import com.nksolucoes.ecommerce.domain.User;
import com.nksolucoes.ecommerce.domain.enumerations.OrderStatusEnum;
import com.nksolucoes.ecommerce.infrastructure.repository.UserRepository;
import com.nksolucoes.ecommerce.security.CurrentUserResolver;
import com.nksolucoes.ecommerce.security.JwtService;
import com.nksolucoes.ecommerce.web.dto.request.OrderItemRequestDTO;
import com.nksolucoes.ecommerce.web.dto.request.OrderRequestDTO;
import com.nksolucoes.ecommerce.web.dto.response.OrderResponseDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService service;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CurrentUserResolver currentUserResolver;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "USER")
    void shouldCreateOrder() throws Exception {
        User user = User.builder().id(UUID.randomUUID()).build();
        OrderRequestDTO dto = new OrderRequestDTO(List.of(new OrderItemRequestDTO(UUID.randomUUID(), 2)));

        OrderResponseDTO response = new OrderResponseDTO(UUID.randomUUID(), OrderStatusEnum.PENDING, BigDecimal.TEN, LocalDateTime.now(), List.of());

        Mockito.when(service.createOrder(any(User.class), any(OrderRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldPayOrder() throws Exception {
        UUID id = UUID.randomUUID();
        OrderResponseDTO response = new OrderResponseDTO(id, OrderStatusEnum.PAID, BigDecimal.TEN, LocalDateTime.now(), List.of());

        Mockito.when(service.payOrder(id)).thenReturn(response);

        mockMvc.perform(post("/orders/" + id + "/pay"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAID"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldListOrders() throws Exception {
        User user = User.builder().id(UUID.randomUUID()).build();
        OrderResponseDTO response = new OrderResponseDTO(UUID.randomUUID(), OrderStatusEnum.PAID, BigDecimal.TEN, LocalDateTime.now(), List.of());

        Mockito.when(service.listUserOrders(any(User.class))).thenReturn(List.of(response));

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk());
    }
}
