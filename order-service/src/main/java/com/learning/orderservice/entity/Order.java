package com.learning.orderservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
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
