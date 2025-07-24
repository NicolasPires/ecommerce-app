package com.nksolucoes.ecommerce.domain;

import com.nksolucoes.ecommerce.domain.enumerations.OrderStatusEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderStatusEnum status;

    private BigDecimal total;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.status = OrderStatusEnum.PENDING;
        this.total = calculateTotal();
    }

    public BigDecimal calculateTotal() {
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItem item : items) {
            if (item.getProduct() != null) {
                BigDecimal price = item.getProduct().getPrice();
                int quantity = item.getQuantity();
                total = total.add(price.multiply(BigDecimal.valueOf(quantity)));
            }
        }

        return total;
    }
}
