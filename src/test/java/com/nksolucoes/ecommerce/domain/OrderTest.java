package com.nksolucoes.ecommerce.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderTest {

    @Test
    void shouldCalculateTotalCorrectly() {
        Product p1 = new Product();
        p1.setPrice(new BigDecimal("10.00"));

        Product p2 = new Product();
        p2.setPrice(new BigDecimal("5.50"));

        Order order = new Order();

        OrderItem item1 = new OrderItem();
        item1.setProduct(p1);
        item1.setQuantity(2);
        item1.setOrder(order);

        OrderItem item2 = new OrderItem();
        item2.setProduct(p2);
        item2.setQuantity(3);
        item2.setOrder(order);

        order.setItems(List.of(item1, item2));

        BigDecimal total = order.calculateTotal();
        assertThat(total).isEqualByComparingTo("36.50");
    }
}
