package com.oteller.hotelservice.controller;

import com.oteller.hotelservice.dto.AddressDto;
import com.oteller.hotelservice.dto.HotelDto;
import com.oteller.hotelservice.services.HotelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HotelControllerTest {

    @Mock
    private HotelService hotelService;

    @InjectMocks
    private HotelController hotelController;

    @Test
    void shouldCreateHotelSuccessfully() {
        HotelDto inputDto = getHotelDto();
        HotelDto savedDto = getHotelDto();
        when(hotelService.create(inputDto)).thenReturn(savedDto);

        // When
        ResponseEntity<HotelDto> response = hotelController.create(inputDto);

        // Then
        verify(hotelService, times(1)).create(inputDto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(inputDto, response.getBody());
    }

    @Test
    void shouldGetByIdSuccessfully() {
        HotelDto hotelDto = getHotelDto();
        when(hotelService.getById(1L)).thenReturn(hotelDto);

        // When
        ResponseEntity<HotelDto> response = hotelController.getById(1L);

        // Then
        verify(hotelService, times(1)).getById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(hotelDto, response.getBody());
    }

    @Test
    void shouldGetAllHotelsSuccessfully() {
        HotelDto hotelDto = getHotelDto();
        when(hotelService.getAllHotels()).thenReturn(Arrays.asList(hotelDto));

        // When
        ResponseEntity<List<HotelDto>> response = hotelController.getAllHotels();

        // Then
        verify(hotelService, times(1)).getAllHotels();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Arrays.asList(hotelDto), response.getBody());
    }

    private HotelDto getHotelDto() {
        return HotelDto.builder()
                .name("Hotel Sky")
                .starRating(5)
                .addressDto(AddressDto.builder()
                        .street("Bug street 12")
                        .city("İstanbul")
                        .country("TR")
                        .build())
                .build();
    }
}
