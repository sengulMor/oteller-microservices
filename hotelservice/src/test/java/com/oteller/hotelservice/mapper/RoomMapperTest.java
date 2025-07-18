package com.oteller.hotelservice.mapper;

import com.oteller.hotelservice.dto.RoomDto;
import com.oteller.hotelservice.model.Hotel;
import com.oteller.hotelservice.model.Room;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoomMapperTest {

    private final RoomMapper roomMapper = Mappers.getMapper(RoomMapper.class);

    @Test
    void shouldMapRoomToRoomDto() {
        Hotel hotel = new Hotel();
        hotel.setId(1L);

        Room room = new Room();
        room.setId(10L);
        room.setRoomNumber("101");
        room.setPricePerNight(new BigDecimal("150.00"));
        room.setAvailable(true);
        room.setHotel(hotel);

        RoomDto dto = roomMapper.toDto(room);

        assertEquals(room.getId(), dto.getId());
        assertEquals(room.getRoomNumber(), dto.getRoomNumber());
        assertEquals(room.getPricePerNight(), dto.getPricePerNight());
        assertEquals(room.isAvailable(), dto.isAvailable());
        assertEquals(hotel.getId(), dto.getHotelId());
    }

    @Test
    void shouldMapRoomDtoToRoom() {
        RoomDto dto = new RoomDto();
        dto.setId(20L);
        dto.setRoomNumber("202");
        dto.setPricePerNight(new BigDecimal("200.00"));
        dto.setAvailable(false);
        dto.setHotelId(2L); // ignored in mapping

        Hotel hotel = new Hotel();
        hotel.setId(2L);

        Room room = roomMapper.toEntity(dto, hotel);

        assertEquals(dto.getId(), room.getId());
        assertEquals(dto.getRoomNumber(), room.getRoomNumber());
        assertEquals(dto.getPricePerNight(), room.getPricePerNight());
        assertEquals(dto.isAvailable(), room.isAvailable());
        assertEquals(hotel, room.getHotel()); // from @AfterMapping
    }

    @Test
    void shouldUpdateExistingRoom() {
        Room room = new Room();
        room.setRoomNumber("Old Number");
        room.setPricePerNight(new BigDecimal("100.00"));
        room.setAvailable(true);

        RoomDto dto = new RoomDto();
        dto.setRoomNumber("Updated Number");
        dto.setPricePerNight(new BigDecimal("250.00"));
        dto.setAvailable(false);

        roomMapper.update(room, dto);

        assertEquals("Updated Number", room.getRoomNumber());
        assertEquals(new BigDecimal("250.00"), room.getPricePerNight());
        assertFalse(room.isAvailable());
    }

    @Test
    void shouldMapRoomListToDtoList() {
        Hotel hotel = new Hotel();
        hotel.setId(1L);

        Room room1 = new Room();
        room1.setId(1L);
        room1.setRoomNumber("A1");
        room1.setPricePerNight(new BigDecimal("100"));
        room1.setAvailable(true);
        room1.setHotel(hotel);

        Room room2 = new Room();
        room2.setId(2L);
        room2.setRoomNumber("B2");
        room2.setPricePerNight(new BigDecimal("150"));
        room2.setAvailable(false);
        room2.setHotel(hotel);

        List<RoomDto> dtoList = roomMapper.toDtoList(List.of(room1, room2));

        assertEquals(2, dtoList.size());
        assertEquals("A1", dtoList.get(0).getRoomNumber());
        assertEquals("B2", dtoList.get(1).getRoomNumber());
        assertEquals(1L, dtoList.get(0).getHotelId());
    }
}
