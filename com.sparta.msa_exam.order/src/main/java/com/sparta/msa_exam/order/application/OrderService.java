package com.sparta.msa_exam.order.application;

import com.sparta.msa_exam.order.api.OrderItemRequestDto;
import com.sparta.msa_exam.order.api.OrderRequestDto;
import com.sparta.msa_exam.order.api.OrderResponseDto;
import com.sparta.msa_exam.order.domain.Order;
import com.sparta.msa_exam.order.domain.OrderItem;
import com.sparta.msa_exam.order.domain.OrderItemRepository;
import com.sparta.msa_exam.order.domain.OrderRepository;
import com.sparta.msa_exam.order.domain.exception.CustomException;
import com.sparta.msa_exam.order.domain.exception.ErrorCode;
import com.sparta.msa_exam.order.infra.ProductClient;
import com.sparta.msa_exam.order.infra.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductClient productClient;

    public OrderResponseDto createOrder(String userId, OrderRequestDto orderRequestDto) {
        Order order = orderRepository.save(Order.from(Long.parseLong(userId)));
        List<OrderItemRequestDto> orderItemRequestList = orderRequestDto.getOrderItems();
        List<OrderItem> orderItemList = getOrderItemList(order, orderItemRequestList);
        order.addOrderItem(orderItemRepository.saveAll(orderItemList));
        return OrderResponseDto.from(order);

    }

    private List<OrderItem> getOrderItemList(Order order, List<OrderItemRequestDto> orderItemRequestList) {
        List<Long> productIdList =
                orderItemRequestList
                        .stream()
                        .map(OrderItemRequestDto::getProductId)
                        .toList();

        // feignclient 로 product service 호출
        List<ProductResponseDto> productList = productClient.getProductsByIdList(productIdList);

        return orderItemRequestList.stream()
                .map(orderItemRequest -> {
                    Optional<ProductResponseDto> productResponseDto = productList.stream()
                            .filter(product -> product.getProductId().equals(orderItemRequest.getProductId()))
                            .findFirst();

                    if (productResponseDto.isEmpty()) {
                        throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
                    }

                    ProductResponseDto product = productResponseDto.get();
                    return OrderItem.of(order, product.getProductId(), orderItemRequest.getQuantity(), orderItemRequest.getUnitPrice());
                })
                .collect(Collectors.toList());
    }

}
