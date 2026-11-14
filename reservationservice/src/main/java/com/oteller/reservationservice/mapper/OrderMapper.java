package com.oteller.reservationservice.mapper;

import com.oteller.reservationservice.dto.OrderDto;
import com.oteller.reservationservice.model.Orders;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Orders toOrder(OrderDto orderDto);

    OrderDto toOrderDto(Orders orders);
}
