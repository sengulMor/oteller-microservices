package com.oteller.hotelservice.service;

import com.oteller.events.RoomReservedEvent;
import com.oteller.hotelservice.dto.RoomAvailabilityDTO;
import com.oteller.hotelservice.dto.RoomDto;
import com.oteller.hotelservice.model.Address;
import com.oteller.hotelservice.model.Hotel;
import com.oteller.hotelservice.model.Room;
import com.oteller.hotelservice.repository.HotelRepository;
import com.oteller.hotelservice.repository.RoomRepository;
import com.oteller.hotelservice.services.KafkaProducer;
import com.oteller.hotelservice.services.RoomService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

    @InjectMocks
    private RoomService roomService;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private KafkaProducer kafkaProducer;

    @Test
    void shouldCreateRoomSuccessfully() {
        //Given
        Hotel hotel = getHotel();
        Room room = getRoom();
        room.setHotel(hotel);
        RoomDto roomDto = getRoomDto();
        when(hotelRepository.findById(any())).thenReturn(Optional.of(hotel));
        when(roomRepository.save(any(Room.class))).thenReturn(room);
        // When
        RoomDto result = roomService.create(roomDto);
        // Then
        verify(roomRepository, times(1)).save(room);
        assertEquals(roomDto.getRoomNumber(), result.getRoomNumber());
        assertEquals(roomDto.getHotelId(), result.getHotelId());
    }

    @Test
    void shouldRoomGetByIdSuccessfully() {
        //Given
        Hotel hotel = getHotel();
        Room room = getRoom();
        room.setHotel(hotel);
        when(roomRepository.findById(2L)).thenReturn(Optional.of(room));
        // When
        RoomDto result = roomService.getById(2L);
        // Then
        verify(roomRepository, times(1)).findById(2L);
        assertEquals(room.getRoomNumber(), result.getRoomNumber());
        assertEquals(room.getHotel().getId(), result.getHotelId());
    }

    @Test
    void shouldThrowExceptionWhenHotelNotFound() {
        //Given
        Long roomId = 1L;
        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());
        //When
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            roomService.getById(roomId);
        });
        //Then
        assertEquals("Room not found", exception.getMessage());
    }

    @Test
    void shouldGetAllRoomsOfHotelSuccessfully() {
        //Given
        Long hotelId = 1L;
        Hotel hotel = getHotel();
        Room room = getRoom();
        room.setHotel(hotel);
        when(roomRepository.findAllByHotelId(hotelId)).thenReturn(Optional.of(Arrays.asList(room)));
        // When
        List<RoomDto> rooms = roomService.getAllRoomsOfHotel(hotelId);
        // Then
        verify(roomRepository, times(1)).findAllByHotelId(hotelId);
        assertEquals(room.getRoomNumber(), rooms.get(0).getRoomNumber());
        assertEquals(room.getHotel().getId(), rooms.get(0).getHotelId());
    }

    @Test
    void shouldReturnTrueWhenRoomIsAvailable() {
        // Given
        Long roomId = 1L;
        Room room = getRoom();
        RoomAvailabilityDTO request = getRoomAvailabilityDto(roomId);
        when(roomRepository.findRoomForUpdate(roomId)).thenReturn(Optional.of(room));
        // When
        Boolean result = roomService.isRoomAvailable(request);
        // Then
        assertTrue(result);
        assertTrue(room.isReserved()); // room should be reserved
        verify(roomRepository).save(room);
        verify(kafkaProducer).sendRoomReservedEvent(any(RoomReservedEvent.class));
    }

    @Test
    void shouldReturnFalseWhenRoomIsNotAvailable() {
        // Given
        Long roomId = 2L;
        Room room = new Room();
        RoomAvailabilityDTO request = getRoomAvailabilityDto(roomId);
        when(roomRepository.findRoomForUpdate(roomId)).thenReturn(Optional.of(room));
        // When
        Boolean result = roomService.isRoomAvailable(request);
        // Then
        assertFalse(result);
        verify(roomRepository, never()).save(any());
        verify(kafkaProducer, never()).sendRoomReservedEvent(any());
    }

    private RoomDto getRoomDto() {
        return RoomDto.builder()
                .roomNumber("3")
                .capacity(2)
                .pricePerNight(BigDecimal.valueOf(70.99))
                .hotelId(2L)
                .build();
    }

    private RoomAvailabilityDTO getRoomAvailabilityDto(Long roomId) {
        return RoomAvailabilityDTO.builder()
                .roomId(roomId)
                .build();
    }


    private Room getRoom() {
        Room room = new Room();
        room.setRoomNumber("3");
        room.setCapacity(2);
        room.setPricePerNight(BigDecimal.valueOf(70.99));
        room.setAvailable(true);
        room.setReserved(false);
        return room;
    }

    private Hotel getHotel() {
        Address address = new Address();
        address.setStreet("Bug street 12");
        address.setCity("İstanbul");
        address.setCountry("TR");
        Hotel hotel = new Hotel();
        hotel.setName("Hotel Sky");
        hotel.setStarRating(5);
        hotel.setAddress(address);
        hotel.setId(2L);
        return hotel;
    }
}
