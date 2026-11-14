package com.oteller.reservationservice.services;

import com.oteller.reservationservice.dto.OrderDto;
import com.oteller.reservationservice.mapper.OrderMapper;
import com.oteller.reservationservice.model.Orders;
import com.oteller.reservationservice.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository,
                        OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }


    @Transactional
    public OrderDto saveOrder(OrderDto orderDto) {
        Orders orders = orderMapper.toOrder(orderDto);
        Orders savedOrders = orderRepository.save(orders);
        return orderMapper.toOrderDto(savedOrders);
    }
}
