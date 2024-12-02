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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        List<ProductResponseDto> productResponseDtoList = getProductList(orderItemRequestList);
        List<OrderItem> orderItemList = toOrderItemList(order, orderItemRequestList, productResponseDtoList);
        order.addOrderItem(orderItemRepository.saveAll(orderItemList));
        return OrderResponseDto.from(order);
    }

    @Transactional(readOnly = true)
    public OrderResponseDto getOrder(String userId, Long orderId) {
        Order order = checkOrder(orderId);
        checkUser(userId, order);
        return OrderResponseDto.from(order);
    }

    public OrderResponseDto updateOrder(String userId, Long orderId, OrderRequestDto orderRequestDto) {
        Order order = checkOrder(orderId);
        checkUser(userId, order);

        // 요청된 주문 아이템과 관련된 상품 정보 가져오기
        List<OrderItemRequestDto> orderItemRequestList = orderRequestDto.getOrderItems();
        List<ProductResponseDto> productResponseDtoList = getProductList(orderItemRequestList);

        // 상품 ID 리스트 생성
        List<Long> productIdList = productResponseDtoList.stream()
                .map(ProductResponseDto::getProductId)
                .toList();

        // 기존 주문 아이템 가져오기
        List<OrderItem> existingOrderItems = orderItemRepository.findOrderItemByOrderAndProductIdIn(order, productIdList);

        // 기존 아이템 업데이트
        existingOrderItems.forEach(orderItem ->
                orderItemRequestList.stream()
                        .filter(requestDto -> requestDto.getProductId().equals(orderItem.getProductId()))
                        .findFirst()
                        .ifPresent(requestDto -> orderItem.update(requestDto.getQuantity(), requestDto.getUnitPrice()))
        );

        // 요청된 상품 중 기존에 없는 상품을 새로운 아이템으로 추가
        List<OrderItem> newOrderItems = orderItemRequestList.stream()
                .filter(requestDto -> existingOrderItems.stream()
                        .noneMatch(orderItem -> orderItem.getProductId().equals(requestDto.getProductId())))
                .map(requestDto -> OrderItem.of(order, requestDto.getProductId(), requestDto.getQuantity(), requestDto.getUnitPrice()))
                .toList();

        List<OrderItem> updatedOrderItems = new ArrayList<>(existingOrderItems);
        updatedOrderItems.addAll(newOrderItems);
        order.addOrderItem(orderItemRepository.saveAll(updatedOrderItems));

        return OrderResponseDto.from(order);
    }


    public Order checkOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
    }

    public void checkUser(String userId, Order order) {
        if(!Objects.equals(Long.parseLong(userId), order.getUserId()))
            throw new CustomException(ErrorCode.USER_NOT_SAME);
    }

    private List<ProductResponseDto> getProductList(List<OrderItemRequestDto> orderItemRequestList) {
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

                    return productResponseDto.get();
                })
                .collect(Collectors.toList());
    }


    public List<OrderItem> toOrderItemList(Order order,
                                           List<OrderItemRequestDto> orderItemRequestList,
                                           List<ProductResponseDto> productResponseDtoList) {
        return orderItemRequestList.stream()
                .map(requestDto -> {
                    ProductResponseDto responseDto = productResponseDtoList.stream()
                            .filter(product -> product.getProductId().equals(requestDto.getProductId()))
                            .findFirst()
                            .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

                    return OrderItem.of(order, responseDto.getProductId(), requestDto.getQuantity(), requestDto.getUnitPrice());
                })
                .toList();
    }

}
