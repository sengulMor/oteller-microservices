package com.oteller.hotelservice.mapper;

import com.oteller.hotelservice.dto.HotelDto;
import com.oteller.hotelservice.model.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = AddressMapper.class)
public interface HotelMapper {

    HotelDto toDto(Hotel hotel);

    Hotel toEntity(HotelDto dto);

    List<HotelDto> toDtoList(List<Hotel> hotels);

    @Mapping(target = "rooms", ignore = true)
    void update(@MappingTarget Hotel hotel, HotelDto dto);
}
