package com.oteller.hotelservice.mapper;

import com.oteller.hotelservice.dto.RoomDto;
import com.oteller.hotelservice.model.Hotel;
import com.oteller.hotelservice.model.Room;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    @Mapping(target = "hotelId", source = "hotel.id")
    RoomDto toDto(Room room);

    @Mapping(target = "hotel", ignore = true)
    Room toEntity(RoomDto roomDto, @Context Hotel hotel);

    List<RoomDto> toDtoList(List<Room> rooms);

    void update(@MappingTarget Room room, RoomDto roomDto);

    @AfterMapping
    default void setHotel(@MappingTarget Room room, @Context Hotel hotel) {
        room.setHotel(hotel);
    }

}

