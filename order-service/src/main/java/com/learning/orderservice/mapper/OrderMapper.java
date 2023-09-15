package com.learning.orderservice.mapper;

import com.learning.orderservice.dto.OrderDto;
import com.learning.orderservice.entity.Order;

public class OrderMapper {

    public static Order toOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setId(orderDto.getId());
        order.setProductId(orderDto.getProductId());
        order.setQuantity(orderDto.getQuantity());
        order.setPrice(orderDto.getPrice());
        order.setDeliveryPrice(orderDto.getDeliveryPrice());
        order.setDiscountPrice(orderDto.getDiscountPrice());
        order.setInsurancePrice(orderDto.getInsurancePrice());
        order.setUserId(orderDto.getUserId());
        order.setAddress(orderDto.getAddress());
        order.setStatus(orderDto.getStatus());
        order.setDate(orderDto.getDate());
        return order;
    }

    public static OrderDto toOrderDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setProductId(order.getProductId());
        orderDto.setQuantity(order.getQuantity());
        orderDto.setPrice(order.getPrice());
        orderDto.setDeliveryPrice(order.getDeliveryPrice());
        orderDto.setDiscountPrice(order.getDiscountPrice());
        orderDto.setInsurancePrice(order.getInsurancePrice());
        orderDto.setUserId(order.getUserId());
        orderDto.setAddress(order.getAddress());
        orderDto.setStatus(order.getStatus());
        orderDto.setDate(order.getDate());
        return orderDto;
    }

}
