package com.oteller.hotelservice.mapper;

import com.oteller.hotelservice.dto.AddressDto;
import com.oteller.hotelservice.dto.HotelDto;
import com.oteller.hotelservice.model.Address;
import com.oteller.hotelservice.model.Hotel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class HotelMapperTest {

    @Autowired
    private HotelMapper hotelMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Test
    void shouldMapHotelToDto() {
        // Given
        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Hilton");
        hotel.setStarRating(5);

        // Example address object
        Address address = new Address();
        address.setStreet("Alte Post str 37");
        address.setCity("Stuttgart");
        address.setCountry("Deutschland");
        address.setZipCode("34521");
        hotel.setAddress(address);

        // When
        HotelDto hotelDto = hotelMapper.toDto(hotel);

        // Then
        assertEquals(hotel.getId(), hotelDto.getId());
        assertEquals(hotel.getName(), hotelDto.getName());
        assertEquals(hotel.getStarRating(), hotelDto.getStarRating());
        assertEquals(hotel.getAddress().getStreet(), hotelDto.getAddress().getStreet());
    }

    @Test
    void shouldMapDtoToHotel() {
        // Given
        HotelDto hotelDto = new HotelDto();
        hotelDto.setId(1L);
        hotelDto.setName("Hilton");
        hotelDto.setStarRating(5);

        AddressDto addressDto = new AddressDto();
        addressDto.setStreet("Alte Post str 37");
        addressDto.setCity("Stuttgart");
        addressDto.setCountry("Deutschland");
        addressDto.setZipCode("34521");
        hotelDto.setAddress(addressDto);

        // When
        Hotel hotel = hotelMapper.toEntity(hotelDto);

        // Then
        assertEquals(hotelDto.getId(), hotel.getId());
        assertEquals(hotelDto.getName(), hotel.getName());
        assertEquals(hotelDto.getStarRating(), hotel.getStarRating());
        assertEquals(hotelDto.getAddress().getStreet(), hotel.getAddress().getStreet());
    }

    @Test
    void shouldMapListOfHotelsToDtos() {
        // Given
        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Hilton");

        List<Hotel> hotels = List.of(hotel);

        // When
        List<HotelDto> hotelDtos = hotelMapper.toDtoList(hotels);

        // Then
        assertEquals(hotels.size(), hotelDtos.size());
        assertEquals(hotels.get(0).getName(), hotelDtos.get(0).getName());
    }

    @Test
    void shouldUpdateHotelFromDto() {
        // Given
        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Old Hotel");
        hotel.setStarRating(3);

        HotelDto hotelDto = new HotelDto();
        hotelDto.setName("Updated Hotel");
        hotelDto.setStarRating(4);

        // When
        hotelMapper.update(hotel, hotelDto);

        // Then
        assertEquals(hotelDto.getName(), hotel.getName());
        assertEquals(hotelDto.getStarRating(), hotel.getStarRating());
    }
}
