package com.oteller.hotelservice.mapper;

import com.oteller.hotelservice.dto.AddressDto;
import com.oteller.hotelservice.model.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = HotelMapper.class)
public interface AddressMapper {

    AddressDto toDto(Address address);

    Address toEntity(AddressDto addressDto);
}
