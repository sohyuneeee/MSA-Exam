package com.sparta.msa_exam.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findOrderItemByOrderAndProductIdIn(Order order, List<Long> productIdList);
}
