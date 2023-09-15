package com.learning.orderservice.service.impl;

import com.learning.orderservice.entity.Order;
import com.learning.orderservice.repository.OrderRepository;
import com.learning.orderservice.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;

    @Override
    public Order saveOrder(Order order) {
        String orderId = "ORDER-" + LocalDate.now() + "-" + order.getUserId() + "-" + order.getProductId() + "-" + orderRepository.count();
        LocalDateTime now = LocalDateTime.now();
        order.setId(orderId);
        order.setDate(now);
        order.setStatus(Order.STATUS_CREATED);
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getProductById(String orderId) {
        Order order = orderRepository.findById(orderId).get();
        return order;
    }

}
