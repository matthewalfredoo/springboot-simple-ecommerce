package com.learning.orderservice.service;

import com.learning.orderservice.entity.Order;

import java.util.List;

public interface OrderService {

    Order saveOrder(Order order);

    List<Order> getAllOrders();

    Order getProductById(String orderId);

}
