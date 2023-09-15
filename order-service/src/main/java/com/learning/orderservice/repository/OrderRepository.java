package com.learning.orderservice.repository;

import com.learning.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findByOrderByDateAsc();

}
