package com.oteller.hotelservice.service;

import com.oteller.hotelservice.dto.HotelDto;
import com.oteller.hotelservice.exception.HotelNotFoundException;
import com.oteller.hotelservice.exception.HotelUpdateException;
import com.oteller.hotelservice.mapper.HotelMapper;
import com.oteller.hotelservice.model.Hotel;
import com.oteller.hotelservice.repository.HotelRepository;
import com.oteller.hotelservice.services.HotelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HotelServiceTest {

    private HotelRepository hotelRepository;
    private HotelMapper hotelMapper;
    private HotelService hotelService;

    @BeforeEach
    void setUp() {
        hotelRepository = mock(HotelRepository.class);
        hotelMapper = mock(HotelMapper.class);
        hotelService = new HotelService(hotelRepository, hotelMapper);
    }

    @Test
    void create_shouldReturnDto_whenSuccess() {
        HotelDto dto = getHotelDto();
        Hotel hotelEntity = new Hotel();
        Hotel savedHotel = new Hotel();
        HotelDto savedDto = getHotelDto();

        when(hotelMapper.toEntity(dto)).thenReturn(hotelEntity);
        when(hotelRepository.save(hotelEntity)).thenReturn(savedHotel);
        when(hotelMapper.toDto(savedHotel)).thenReturn(savedDto);

        HotelDto result = hotelService.create(dto);

        assertEquals(savedDto, result);
        verify(hotelRepository).save(hotelEntity);
    }

    @Test
    void create_shouldThrowDataIntegrityViolationException() {
        HotelDto dto = getHotelDto();
        Hotel hotel = new Hotel();

        when(hotelMapper.toEntity(dto)).thenReturn(hotel);
        when(hotelRepository.save(hotel)).thenThrow(new DataIntegrityViolationException("Unique constraint failed"));

        assertThrows(DataIntegrityViolationException.class, () -> hotelService.create(dto));
    }

    @Test
    void create_shouldThrowRuntimeException_forUnexpectedException() {
        HotelDto dto = getHotelDto();
        Hotel hotel = new Hotel();

        when(hotelMapper.toEntity(dto)).thenReturn(hotel);
        when(hotelRepository.save(hotel)).thenThrow(new RuntimeException("Database down"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> hotelService.create(dto));
        assertTrue(exception.getMessage().contains("Unexpected error"));
    }

    @Test
    void getAllHotels_shouldReturnList() {
        List<Hotel> hotels = List.of(new Hotel());
        List<HotelDto> dtos = List.of(getHotelDto());

        when(hotelRepository.findAll()).thenReturn(hotels);
        when(hotelMapper.toDtoList(hotels)).thenReturn(dtos);

        List<HotelDto> result = hotelService.getAllHotels();
        assertEquals(dtos, result);
    }

    @Test
    void getById_shouldReturnHotelDto_whenFound() {
        Hotel hotel = new Hotel();
        HotelDto dto = getHotelDto();

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(hotelMapper.toDto(hotel)).thenReturn(dto);

        HotelDto result = hotelService.getById(1L);
        assertEquals(dto, result);
    }

    @Test
    void getById_shouldThrowException_whenNotFound() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(HotelNotFoundException.class, () -> hotelService.getById(1L));
    }

    @Test
    void deleteById_shouldDelete_whenFound() {
        Hotel hotel = new Hotel();
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));

        hotelService.deleteById(1L);

        verify(hotelRepository).delete(hotel);
    }

    @Test
    void deleteById_shouldThrowException_whenNotFound() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(HotelNotFoundException.class, () -> hotelService.deleteById(1L));
    }

    @Test
    void update_shouldReturnUpdatedDto_whenSuccess() {
        HotelDto dto = getHotelDto();
        dto.setId(1L);

        Hotel existingHotel = new Hotel();
        Hotel updatedHotel = new Hotel();
        HotelDto updatedDto = getHotelDto();

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(existingHotel));
        doNothing().when(hotelMapper).update(existingHotel, dto);
        when(hotelRepository.save(existingHotel)).thenReturn(updatedHotel);
        when(hotelMapper.toDto(updatedHotel)).thenReturn(updatedDto);

        HotelDto result = hotelService.update(dto);
        assertEquals(updatedDto, result);
    }

    @Test
    void update_shouldThrowHotelNotFoundException_whenHotelNotFound() {
        HotelDto dto = getHotelDto();
        dto.setId(999L);

        when(hotelRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(HotelNotFoundException.class, () -> hotelService.update(dto));
    }

    @Test
    void update_shouldThrowHotelUpdateException_whenConstraintFails() {
        HotelDto dto = getHotelDto();
        dto.setId(1L);
        Hotel hotel = new Hotel();

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        doThrow(new DataIntegrityViolationException("Duplicate")).when(hotelRepository).save(hotel);

        assertThrows(HotelUpdateException.class, () -> hotelService.update(dto));
    }

    private HotelDto getHotelDto() {
        HotelDto dto = new HotelDto();
        dto.setName("Test Hotel");
        return dto;
    }
}

