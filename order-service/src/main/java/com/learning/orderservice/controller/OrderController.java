package com.learning.orderservice.controller;

import com.learning.orderservice.dto.ApiResponseDto;
import com.learning.orderservice.dto.OrderDto;
import com.learning.orderservice.entity.Order;
import com.learning.orderservice.mapper.OrderMapper;
import com.learning.orderservice.proxy.AuthServiceProxy;
import com.learning.orderservice.proxy.ProductServiceProxy;
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
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@AllArgsConstructor
@SuppressWarnings("unchecked")
public class OrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    private OrderService orderService;

    private JwtService jwtService;

    private AuthServiceProxy authServiceProxy;

    private ProductServiceProxy productServiceProxy;

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

        // check the role of user
        String role = claimsJws.getBody().get("role", String.class);
        if(role.equals("admin")) {
            ApiResponseDto apiResponseDto = new ApiResponseDto();
            apiResponseDto.setSuccess(false);
            apiResponseDto.setMessage("Only customers can place orders");
            apiResponseDto.setTimestamp(LocalDateTime.now().toString());
            apiResponseDto.setData(null);

            return new ResponseEntity<>(apiResponseDto, HttpStatus.FORBIDDEN);
        }

        // get the user ID
        String userId = claimsJws.getBody().get("sub", String.class);

        order.setUserId(Long.parseLong(userId));

        // get the user's address for future reference, in case the address changes
        ResponseEntity<ApiResponseDto> responseEntityUserById = authServiceProxy.getUserById(order.getUserId());
        ApiResponseDto apiResponseDtoUserById = responseEntityUserById.getBody();

        // if the auth service is down, return a fallback response from AuthServiceProxyFallback
        if(apiResponseDtoUserById == null) {
            return responseEntityUserById;
        }

        // TODO if is not successful, should return ResourceNotFoundException
        if(!apiResponseDtoUserById.isSuccess() && responseEntityUserById.getStatusCode() != HttpStatus.OK) {
            return responseEntityUserById;
        }

        // if the auth service is up, get the user's address
        if(apiResponseDtoUserById.getData() instanceof LinkedHashMap) {
            LinkedHashMap<String, Object> userDtoMap = (LinkedHashMap<String, Object>) apiResponseDtoUserById.getData();

            order.setAddress(
                    userDtoMap.get("address").toString()
            );
        }

        // get the product's current price for future reference, in case the price changes
        ResponseEntity<ApiResponseDto> responseEntityProductById = productServiceProxy.getProductById(order.getProductId());
        ApiResponseDto apiResponseDtoProductById = responseEntityProductById.getBody();

        // if the product service is down, return a fallback response from ProductServiceProxyFallback
        if(apiResponseDtoProductById == null) {
            return responseEntityProductById;
        }

        // TODO if is not successful, should return ResourceNotFoundException
        if(!apiResponseDtoProductById.isSuccess() && responseEntityProductById.getStatusCode() != HttpStatus.OK) {
            return responseEntityProductById;
        }

        // if the product service is up, get the product's current price
        if(apiResponseDtoProductById.getData() instanceof LinkedHashMap) {
            LinkedHashMap<String, Object> productDtoMap = (LinkedHashMap<String, Object>) apiResponseDtoProductById.getData();

            order.setPrice(
                    Double.parseDouble(productDtoMap.get("price").toString())
            );
        }

        // save the order
        Order savedOrder = orderService.saveOrder(order);

        // map the saved order to an order dto
        OrderDto savedOrderDto = OrderMapper.toOrderDto(savedOrder);

        ApiResponseDto apiResponseDto = new ApiResponseDto();
        apiResponseDto.setSuccess(true);
        apiResponseDto.setMessage("Order saved successfully");
        apiResponseDto.setTimestamp(LocalDateTime.now().toString());
        apiResponseDto.setData(savedOrderDto);

        return new ResponseEntity<>(apiResponseDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto> getAllOrders(
            @RequestHeader("Authorization")
            String authorizationHeader
    ) {
        // get the user id from the token (authorizationHeader)
        Jws<Claims> claimsJws = jwtService.validateToken(authorizationHeader);
        String role = claimsJws.getBody().get("role", String.class);

        List<Order> orders;

        if(!role.equals("admin")) {
            orders = orderService.getOrdersByUserId(Long.parseLong(claimsJws.getBody().get("sub", String.class)));
        }
        else {
            orders = orderService.getAllOrders();
        }

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
            String orderId,

            @RequestHeader("Authorization")
            String authorizationHeader
    ) {
        Order order = orderService.getProductById(orderId);

        // get the user id from the token (authorizationHeader)
        Jws<Claims> claimsJws = jwtService.validateToken(authorizationHeader);
        String role = claimsJws.getBody().get("role", String.class);
        Long userId = Long.parseLong(claimsJws.getBody().get("sub", String.class));

        if(!role.equals("admin") && !order.getUserId().equals(userId)) {
            ApiResponseDto apiResponseDto = new ApiResponseDto();
            apiResponseDto.setSuccess(false);
            apiResponseDto.setMessage("You are not authorized to view this order");
            apiResponseDto.setTimestamp(LocalDateTime.now().toString());
            apiResponseDto.setData(null);

            return new ResponseEntity<>(apiResponseDto, HttpStatus.FORBIDDEN);
        }

        OrderDto orderDto = OrderMapper.toOrderDto(order);

        ApiResponseDto apiResponseDto = new ApiResponseDto();
        apiResponseDto.setSuccess(true);
        apiResponseDto.setMessage("Order fetched successfully");
        apiResponseDto.setTimestamp(LocalDateTime.now().toString());
        apiResponseDto.setData(orderDto);

        return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
    }

}
