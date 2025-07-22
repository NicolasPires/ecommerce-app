package com.nksolucoes.ecommerce.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderTest {

    @Test
    void shouldCalculateTotalCorrectly() {
        Product p1 = Product.builder().price(new BigDecimal("10.00")).build();
        Product p2 = Product.builder().price(new BigDecimal("5.50")).build();

        Order order = new Order();
        OrderItem item1 = OrderItem.builder().product(p1).quantity(2).order(order).build();
        OrderItem item2 = OrderItem.builder().product(p2).quantity(3).order(order).build();

        order.setItems(List.of(item1, item2));

        BigDecimal total = order.calculateTotal();
        assertThat(total).isEqualByComparingTo("36.50");
    }
}

