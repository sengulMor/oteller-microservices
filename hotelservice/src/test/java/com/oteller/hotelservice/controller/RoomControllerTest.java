package com.oteller.hotelservice.controller;

import com.oteller.hotelservice.dto.RoomAvailabilityDTO;
import com.oteller.hotelservice.dto.RoomDto;
import com.oteller.hotelservice.services.RoomService;
import jakarta.validation.Valid;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoomControllerTest {

    @Mock
    private RoomService roomService;

    @InjectMocks
    private RoomController roomController;


    @PostMapping
    public ResponseEntity<RoomDto> create(@Valid @RequestBody RoomDto roomDto) {
        RoomDto saved = roomService.create(roomDto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
    @Test
    void shouldCreateRoomSuccessfully() {
        RoomDto inputDto = getRoomDto();
        RoomDto savedDto = getRoomDto();
        when(roomService.create(inputDto)).thenReturn(savedDto);

        // When
        ResponseEntity<RoomDto> response = roomController.create(inputDto);

        // Then
        verify(roomService, times(1)).create(inputDto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(inputDto, response.getBody());
    }

    @Test
    void shouldGetByIdSuccessfully() {
        Long roomId = 1L;
        RoomDto roomDto = getRoomDto();
        when(roomService.getById(roomId)).thenReturn(roomDto);

        // When
        ResponseEntity<RoomDto> response = roomController.getById(roomId);

        // Then
        verify(roomService, times(1)).getById(roomId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(roomDto, response.getBody());
    }


    @Test
    void shouldGetAllRoomsOfHotelSuccessfully() {
        Long hotelId = 1L;
        RoomDto roomDto = getRoomDto();
        when(roomService.getAllRoomsOfHotel(hotelId)).thenReturn(Arrays.asList(roomDto));

        // When
        ResponseEntity<List<RoomDto>> response = roomController.getAllRoomsOfHotel(hotelId);

        // Then
        verify(roomService, times(1)).getAllRoomsOfHotel(hotelId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Arrays.asList(roomDto), response.getBody());
    }


    @Test
    void shouldCheckAvailabilitySuccessfully() {
        RoomAvailabilityDTO roomAvailabilityDTO = RoomAvailabilityDTO.builder().roomId(1L).build();
        when(roomService.isRoomAvailable(roomAvailabilityDTO)).thenReturn(true);

        // When
        ResponseEntity<Boolean> response = roomController.checkAvailability(roomAvailabilityDTO);

        // Then
        verify(roomService, times(1)).isRoomAvailable(roomAvailabilityDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
    }

    private RoomDto getRoomDto() {
        return RoomDto.builder()
                .roomNumber("3")
                .capacity(2)
                .pricePerNight(BigDecimal.valueOf(70.99))
                .hotelId(2L)
                .build();
    }
}