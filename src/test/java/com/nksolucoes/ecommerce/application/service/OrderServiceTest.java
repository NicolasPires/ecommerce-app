package com.nksolucoes.ecommerce.application.service;

import com.nksolucoes.ecommerce.domain.*;
import com.nksolucoes.ecommerce.domain.enumerations.OrderStatusEnum;
import com.nksolucoes.ecommerce.infrastructure.repository.OrderRepository;
import com.nksolucoes.ecommerce.infrastructure.repository.ProductRepository;
import com.nksolucoes.ecommerce.web.dto.request.OrderItemRequestDTO;
import com.nksolucoes.ecommerce.web.dto.request.OrderRequestDTO;
import com.nksolucoes.ecommerce.web.dto.response.OrderResponseDTO;
import com.nksolucoes.ecommerce.web.mapper.OrderMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private ProductRepository productRepository;
    @Mock private OrderMapper mapper;

    @InjectMocks
    private OrderService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateOrderSuccessfully() {
        UUID productId = UUID.randomUUID();
        Product product = Product.builder()
                .id(productId)
                .name("Product A")
                .stockQuantity(10)
                .price(new BigDecimal("100.00"))
                .build();

        OrderItemRequestDTO itemDTO = new OrderItemRequestDTO(productId, 2);
        OrderRequestDTO requestDTO = new OrderRequestDTO(List.of(itemDTO));

        User user = User.builder().id(UUID.randomUUID()).build();
        Order order = Order.builder().user(user).build();
        Order savedOrder = Order.builder().id(UUID.randomUUID()).user(user).build();
        OrderResponseDTO responseDTO = new OrderResponseDTO(
                savedOrder.getId(),
                OrderStatusEnum.PAID,
                BigDecimal.valueOf(200),
                LocalDateTime.now(),
                List.of()
        );

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(mapper.toResponse(savedOrder)).thenReturn(responseDTO);

        OrderResponseDTO result = service.createOrder(user, requestDTO);

        assertThat(result.id()).isEqualTo(savedOrder.getId());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void shouldThrowWhenStockIsInsufficient() {
        UUID productId = UUID.randomUUID();
        Product product = Product.builder()
                .id(productId)
                .name("Product A")
                .stockQuantity(1)
                .build();

        OrderItemRequestDTO itemDTO = new OrderItemRequestDTO(productId, 5);
        OrderRequestDTO requestDTO = new OrderRequestDTO(List.of(itemDTO));
        User user = User.builder().id(UUID.randomUUID()).build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> service.createOrder(user, requestDTO))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Insufficient stock for product");

        verify(orderRepository, never()).save(any());
    }

    @Test
    void shouldPayOrderSuccessfully() {
        UUID orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        Product product = Product.builder()
                .id(productId)
                .name("Produto X")
                .stockQuantity(10)
                .build();

        OrderItem item = OrderItem.builder().product(product).quantity(2).build();
        Order order = Order.builder()
                .id(orderId)
                .status(OrderStatusEnum.PENDING)
                .items(List.of(item))
                .build();

        OrderResponseDTO responseDTO = new OrderResponseDTO(
                orderId,
                OrderStatusEnum.PAID,
                BigDecimal.valueOf(200),
                LocalDateTime.now(),
                List.of()
        );

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(productRepository.save(product)).thenReturn(product);
        when(mapper.toResponse(order)).thenReturn(responseDTO);

        OrderResponseDTO result = service.payOrder(orderId);

        assertThat(result.status()).isEqualTo(OrderStatusEnum.PAID);
        assertThat(product.getStockQuantity()).isEqualTo(8); // 10 - 2
        verify(productRepository).save(product);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void shouldCancelOrderWhenInsufficientStockDuringPayment() {
        UUID orderId = UUID.randomUUID();
        Product product = Product.builder()
                .id(UUID.randomUUID())
                .name("Produto Y")
                .stockQuantity(1)
                .build();

        OrderItem item = OrderItem.builder().product(product).quantity(5).build();
        Order order = Order.builder()
                .id(orderId)
                .status(OrderStatusEnum.PENDING)
                .items(List.of(item))
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        assertThatThrownBy(() -> service.payOrder(orderId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Insufficient stock for product");

        assertThat(order.getStatus()).isEqualTo(OrderStatusEnum.CANCELLED);
        verify(orderRepository).save(order);
    }

    @Test
    void shouldListOrdersForUser() {
        User user = User.builder().id(UUID.randomUUID()).build();
        Order order = Order.builder().id(UUID.randomUUID()).user(user).build();
        OrderResponseDTO response = new OrderResponseDTO(
                order.getId(),
                OrderStatusEnum.PAID,
                BigDecimal.valueOf(200),
                LocalDateTime.now(),
                List.of()
        );

        when(orderRepository.findByUser(user)).thenReturn(List.of(order));
        when(mapper.toResponse(order)).thenReturn(response);

        List<OrderResponseDTO> results = service.listUserOrders(user);

        assertThat(results).hasSize(1);
        assertThat(results.getFirst().id()).isEqualTo(order.getId());
    }

    @Test
    void shouldFindOrderByIdSuccessfully() {
        UUID orderId = UUID.randomUUID();
        User user = User.builder().id(UUID.randomUUID()).build();
        Order order = Order.builder()
                .id(orderId)
                .user(user)
                .status(OrderStatusEnum.PAID)
                .total(BigDecimal.valueOf(100))
                .createdAt(LocalDateTime.now())
                .build();

        OrderResponseDTO responseDTO = new OrderResponseDTO(
                order.getId(),
                order.getStatus(),
                order.getTotal(),
                order.getCreatedAt(),
                List.of()
        );

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(mapper.toResponse(order)).thenReturn(responseDTO);

        OrderResponseDTO result = service.findById(orderId);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(orderId);
        assertThat(result.status()).isEqualTo(OrderStatusEnum.PAID);
        verify(orderRepository).findById(orderId);
        verify(mapper).toResponse(order);
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        UUID orderId = UUID.randomUUID();
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(orderId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Order not found");

        verify(orderRepository).findById(orderId);
    }

    @Test
    void shouldThrowWhenOrderAlreadyProcessed() {
        UUID orderId = UUID.randomUUID();
        Order order = Order.builder()
                .id(orderId)
                .status(OrderStatusEnum.PAID) // <- status já processado
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> service.payOrder(orderId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Order already processed.");

        verify(orderRepository).findById(orderId);
        verify(orderRepository, never()).save(any());
        verify(productRepository, never()).save(any());
    }

}
