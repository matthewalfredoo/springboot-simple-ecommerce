package com.learning.orderservice.service;

import com.learning.orderservice.entity.Order;

import java.util.List;

public interface OrderService {

    Order saveOrder(Order order);

    List<Order> getAllOrders();

    List<Order> getOrdersByUserId(Long userId);

    Order getProductById(String orderId);

}
