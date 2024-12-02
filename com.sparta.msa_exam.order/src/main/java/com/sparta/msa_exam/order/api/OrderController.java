package com.sparta.msa_exam.order.api;

import com.sparta.msa_exam.order.application.OrderService;
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

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(@RequestHeader(value = "X-User-Id") String userId,
                                                     @PathVariable Long orderId) {
        OrderResponseDto responseDto = orderService.getOrder(userId, orderId);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> updateOrder(@RequestHeader(value = "X-User-Id") String userId,
                                                        @PathVariable Long orderId,
                                                        @Valid @RequestBody OrderRequestDto requestDto) {
        OrderResponseDto responseDto = orderService.updateOrder(userId, orderId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

}
