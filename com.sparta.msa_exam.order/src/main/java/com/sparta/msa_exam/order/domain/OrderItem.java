package com.sparta.msa_exam.order.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_items")
public class OrderItem extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int unitPrice;

    @Builder
    private OrderItem(Order order, Long productId, int quantity, int unitPrice) {
        this.order = order;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public static OrderItem of(Order order, Long productId, int quantity, int unitPrice) {
        return OrderItem.builder()
                .order(order)
                .productId(productId)
                .quantity(quantity)
                .unitPrice(unitPrice)
                .build();
    }

    public void update(int quantity, int unitPrice) {
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

}
