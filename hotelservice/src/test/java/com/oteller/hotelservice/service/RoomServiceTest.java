package com.oteller.hotelservice.service;

import com.oteller.events.RoomReservedEvent;
import com.oteller.hotelservice.dto.RoomAvailabilityDTO;
import com.oteller.hotelservice.dto.RoomDto;
import com.oteller.hotelservice.exception.HotelNotFoundException;
import com.oteller.hotelservice.exception.RoomNotFoundException;
import com.oteller.hotelservice.kafka.producer.RoomReservedEventProducer;
import com.oteller.hotelservice.mapper.RoomMapper;
import com.oteller.hotelservice.model.Address;
import com.oteller.hotelservice.model.Hotel;
import com.oteller.hotelservice.model.Room;
import com.oteller.hotelservice.repository.HotelRepository;
import com.oteller.hotelservice.repository.RoomRepository;
import com.oteller.hotelservice.services.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private RoomReservedEventProducer roomReservedEventProducer;

    @Mock
    private RoomMapper roomMapper;

    @InjectMocks
    private RoomService roomService;

    private RoomDto roomDto;
    private Hotel hotel;
    private Address address;
    private Room room;
    Long hotelId = 4L;

    @BeforeEach
    void setUp() {
        address = new Address("Max street 3", "Ankara", "Turkey", "35126", null);
        hotel = new Hotel("Test Hotel", 3, address, null);
        hotel.setId(hotelId);
        roomDto = RoomDto.builder()
                .id(1L)
                .roomNumber("101")
                .available(true)
                .hotelId(hotel.getId())
                .build();
        room = new Room("101", 2, new BigDecimal("40.99"), true, "", null, null, hotel);
    }

    @Test
    void shouldCreateRoomSuccessfully() {
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));
        when(roomMapper.toEntity(roomDto, hotel)).thenReturn(room);
        when(roomRepository.save(room)).thenReturn(room);
        when(roomMapper.toDto(room)).thenReturn(roomDto);

        RoomDto result = roomService.create(roomDto);

        assertNotNull(result);
        assertEquals(roomDto.getRoomNumber(), result.getRoomNumber());
        verify(roomRepository).save(room);
    }

    @Test
    void shouldThrowHotelNotFoundException_whenHotelDoesNotExist() {
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.empty());

        assertThrows(HotelNotFoundException.class, () -> roomService.create(roomDto));
    }

    @Test
    void shouldThrowRoomNotFoundException_whenNoRoomsExistForHotel() {
        when(roomRepository.findAllByHotelId(hotelId)).thenReturn(Collections.emptyList());
        assertThrows(RoomNotFoundException.class, () -> roomService.getAllRoomsOfHotel(hotelId));
    }

    @Test
    void shouldReserveRoomIfAvailable() {
        RoomAvailabilityDTO dto = new RoomAvailabilityDTO(1L, 1L, "John", LocalDate.now(), LocalDate.now().plusDays(3), "johntest@gmail.com");
        Room roomToReserve = new Room("2", 2, new BigDecimal("40.99"), true, "", null, null, hotel);
        roomToReserve.setId(1L);
        when(roomRepository.findRoomForUpdate(1L, 1L)).thenReturn(Optional.of(roomToReserve));
        when(roomRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        boolean reserved = roomService.reserveIfAvailable(dto);

        assertTrue(reserved);
        verify(roomReservedEventProducer).sendRoomReservedEvent(any(RoomReservedEvent.class));
    }

    @Test
    void shouldReturnFalse_whenRoomNotAvailable() {
        RoomAvailabilityDTO dto = new RoomAvailabilityDTO(1L, 1L, "John", LocalDate.now(), LocalDate.now().plusDays(3), "johntest@gmail.com");
        room.setAvailable(false);

        when(roomRepository.findRoomForUpdate(1L, 1L)).thenReturn(Optional.of(room));

        boolean result = roomService.reserveIfAvailable(dto);

        assertFalse(result);
        verify(roomReservedEventProducer, never()).sendRoomReservedEvent(any());
    }

    @Test
    void shouldReturnRoomById() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(roomMapper.toDto(room)).thenReturn(roomDto);

        RoomDto result = roomService.getById(1L);

        assertEquals(roomDto.getId(), result.getId());
    }

    @Test
    void shouldThrowRoomNotFoundException_whenRoomNotFound() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RoomNotFoundException.class, () -> roomService.getById(1L));
    }

    @Test
    void shouldDeleteRoomSuccessfully() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        roomService.deleteById(1L);
        verify(roomRepository).delete(room);
    }

    @Test
    void shouldUpdateRoomSuccessfully() {
        // Given
        Long roomId = 1L;
        RoomDto dto = new RoomDto();
        dto.setId(roomId);
        dto.setHotelId(hotelId);

        Room updatedRoom = new Room();

        when(roomRepository.findByIdAndHotel_Id(roomId, hotelId)).thenReturn(Optional.of(room));
        doNothing().when(roomMapper).update(room, dto);
        when(roomRepository.save(room)).thenReturn(updatedRoom);
        when(roomMapper.toDto(updatedRoom)).thenReturn(roomDto);

        // When
        RoomDto result = roomService.update(dto);

        // Then
        assertEquals(roomDto, result);
        verify(roomRepository).findByIdAndHotel_Id(roomId, hotelId);
        verify(roomMapper).update(room, dto);
        verify(roomRepository).save(room);
        verify(roomMapper).toDto(updatedRoom);
    }

    @Test
    void shouldThrowRoomNotFoundException_whenRoomNotExists() {
        Long roomId = 1L;
        when(roomRepository.findByIdAndHotel_Id(roomId, hotelId)).thenReturn(Optional.empty());

        assertThrows(RoomNotFoundException.class, () -> roomService.update(roomDto));
        verify(roomRepository).findByIdAndHotel_Id(roomId, hotelId);
        verifyNoMoreInteractions(roomMapper, roomRepository);
    }

}
