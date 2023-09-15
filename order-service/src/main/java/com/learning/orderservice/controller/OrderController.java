package com.learning.orderservice.controller;

import com.learning.orderservice.dto.ApiResponseDto;
import com.learning.orderservice.dto.OrderDto;
import com.learning.orderservice.entity.Order;
import com.learning.orderservice.mapper.OrderMapper;
import com.learning.orderservice.service.JwtService;
import com.learning.orderservice.service.OrderService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@AllArgsConstructor
public class OrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    private OrderService orderService;

    private JwtService jwtService;

    @PostMapping
    public ResponseEntity<ApiResponseDto> saveOrder(
            @RequestBody
            OrderDto orderDto,

            @RequestHeader("Authorization")
            String authorizationHeader
    ) {
        Order order = OrderMapper.toOrder(orderDto);

        // get the user id from the token (authorizationHeader)
        Jws<Claims> claimsJws = jwtService.validateToken(authorizationHeader);
        String userId = claimsJws.getBody().get("sub", String.class);

        order.setUserId(Long.parseLong(userId));

        // get the user's address for future reference, in case the address changes
        // dummy address for now
        order.setAddress("123 Main St, New York, NY 10030");

        // get the product's current price for future reference, in case the price changes
        // dummy price for now
        order.setPrice(100.0);

        Order savedOrder = orderService.saveOrder(order);
        
        OrderDto savedOrderDto = OrderMapper.toOrderDto(savedOrder);

        ApiResponseDto apiResponseDto = new ApiResponseDto();
        apiResponseDto.setSuccess(true);
        apiResponseDto.setMessage("Order saved successfully");
        apiResponseDto.setTimestamp(LocalDateTime.now().toString());
        apiResponseDto.setData(savedOrderDto);

        return new ResponseEntity<>(apiResponseDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();

        List<OrderDto> orderDtos = orders.stream().map(OrderMapper::toOrderDto).toList();

        ApiResponseDto apiResponseDto = new ApiResponseDto();
        apiResponseDto.setSuccess(true);
        apiResponseDto.setMessage("Orders fetched successfully");
        apiResponseDto.setTimestamp(LocalDateTime.now().toString());
        apiResponseDto.setData(orderDtos);

        return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponseDto> getOrderById(
            @PathVariable(name = "orderId")
            String orderId
    ) {
        Order order = orderService.getProductById(orderId);

        OrderDto orderDto = OrderMapper.toOrderDto(order);

        ApiResponseDto apiResponseDto = new ApiResponseDto();
        apiResponseDto.setSuccess(true);
        apiResponseDto.setMessage("Order fetched successfully");
        apiResponseDto.setTimestamp(LocalDateTime.now().toString());
        apiResponseDto.setData(orderDto);

        return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
    }

}
