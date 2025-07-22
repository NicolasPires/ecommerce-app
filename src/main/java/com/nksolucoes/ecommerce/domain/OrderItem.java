package com.nksolucoes.ecommerce.domain;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    private Product product;

    @ManyToOne(optional = false)
    private Order order;

    @Column(nullable = false)
    private int quantity;

    public UUID getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public Order getOrder() {
        return order;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
