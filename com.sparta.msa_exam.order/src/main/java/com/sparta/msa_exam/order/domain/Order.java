package com.sparta.msa_exam.order.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItemList = new ArrayList<>();

    @Column(nullable = false)
    private Long userId;

    @Builder
    private Order(Long userId) {
        this.userId = userId;
    }

    public static Order from(Long userId) {
        return Order.builder()
                .userId(userId)
                .build();
    }

    public void addOrderItem(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

}
