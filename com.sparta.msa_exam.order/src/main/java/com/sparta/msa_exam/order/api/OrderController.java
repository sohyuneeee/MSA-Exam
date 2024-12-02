package com.sparta.msa_exam.order.api;

import com.sparta.msa_exam.order.application.OrderService;
import com.sparta.msa_exam.order.domain.Order;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping()
    public ResponseEntity<OrderResponseDto> createOrder(@RequestHeader(value = "X-User-Id") String userId,
                                            @Valid @RequestBody OrderRequestDto requestDto) {
        OrderResponseDto responseDto = orderService.createOrder(userId, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
