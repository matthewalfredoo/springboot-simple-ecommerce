package com.learning.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    String id;

    Long productId;

    int quantity;

    Double price;

    Double deliveryPrice;

    Double discountPrice;

    Double insurancePrice;

    Long userId;

    String address;

    String status;

    LocalDateTime date;

}
