package com.oteller.hotelservice.service;

import com.oteller.hotelservice.dto.AddressDto;
import com.oteller.hotelservice.dto.HotelDto;
import com.oteller.hotelservice.model.Address;
import com.oteller.hotelservice.model.Hotel;
import com.oteller.hotelservice.repository.HotelRepository;
import com.oteller.hotelservice.services.HotelService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HotelServiceTest {

    @InjectMocks
    private HotelService hotelService;

    @Mock
    private HotelRepository hotelRepository;

    @Test
    void shouldCreateHotelSuccessfully() {
        //Given
        Hotel hotel = getHotel();
        HotelDto hotelDto = getHotelDto();
        when(hotelRepository.save(any(Hotel.class))).thenReturn(hotel);
        // When
        HotelDto result = hotelService.create(hotelDto);
        // Then
        verify(hotelRepository, times(1)).save(hotel);
        assertEquals(hotelDto.getName(), result.getName());
        assertEquals(hotelDto.getAddressDto(), result.getAddressDto());
    }



    @Test
    void shouldGetByIdSuccessfully() {
        //Given
        Long hotelId = 1L;
        Hotel hotel = getHotel();
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        //When
        HotelDto result = hotelService.getById(hotelId);
        //Then
        verify(hotelRepository, times(1)).findById(hotelId);
        assertEquals(hotel.getName(), result.getName());
        assertEquals(hotel.getAddress().getStreet(), result.getAddressDto().getStreet());
    }

    @Test
    void shouldThrowExceptionWhenHotelNotFound() {
        //Given
        Long hotelId = 1L;
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.empty());
        //When
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            hotelService.getById(hotelId);
        });
        //Then
        assertEquals("Hotel not found", exception.getMessage());
    }

   @Test
    void shouldGetAllHotelsSuccessfully() {
       // Given
       Hotel hotel = getHotel();
       when(hotelRepository.findAll()).thenReturn(Arrays.asList(hotel));
       //When
       List<HotelDto> hotels = hotelService.getAllHotels();
       //Then
       verify(hotelRepository, times(1)).findAll();
       assertEquals(1, hotels.size());
       assertEquals(hotel.getName(), hotels.get(0).getName());
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

    private Hotel getHotel() {
        Address address = new Address();
        address.setStreet("Bug street 12");
        address.setCity("İstanbul");
        address.setCountry("TR");
        Hotel hotel = new Hotel();
        hotel.setName("Hotel Sky");
        hotel.setStarRating(5);
        hotel.setAddress(address);
        return hotel;
    }
}
