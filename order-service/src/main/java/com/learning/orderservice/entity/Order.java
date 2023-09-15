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
    private String id;

    private Long productId;

    private int quantity;

    private Double price;

    private Double deliveryPrice;

    private Double discountPrice;

    private Double insurancePrice;

    private Long userId;

    private String address;

    private String status;

    private LocalDateTime date;

    public static final String STATUS_CREATED = "CREATED";
    public static final String STATUS_CONFIRMED = "CONFIRMED";
    public static final String STATUS_PACKED = "PACKED";
    public static final String STATUS_SHIPPED = "SHIPPED";
    public static final String STATUS_DELIVERED = "DELIVERED";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_CANCELLED = "CANCELLED";

}

// The data in JSON format for the above entity is as follows:
//{
//    "id": "1",
//    "productId": 1,
//    "quantity": 1,
//    "price": 100.0,
//    "deliveryPrice": 10.0,
//    "discountPrice": 0.0,
//    "insurancePrice": 0.0,
//    "userId": 1,
//    "address": "Address 1",
//    "status": "CREATED",
//    "date": "2021-08-01T00:00:00"
//}
