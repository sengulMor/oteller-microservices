package com.oteller.hotelservice.mapper;

import com.oteller.hotelservice.dto.AddressDto;
import com.oteller.hotelservice.model.Address;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AddressMapperTest {

    // Create an instance of AddressMapper
    private final AddressMapper addressMapper = Mappers.getMapper(AddressMapper.class);

    @Test
    void shouldMapAddressToDto() {
        // Given
        Address address = new Address();
        address.setStreet("Alte Post str 37");
        address.setCity("Stuttgart");
        address.setCountry("Deutschland");
        address.setZipCode("34521");

        // When
        AddressDto addressDto = addressMapper.toDto(address);

        // Then
        assertEquals(address.getStreet(), addressDto.getStreet());
        assertEquals(address.getCity(), addressDto.getCity());
        assertEquals(address.getCountry(), addressDto.getCountry());
        assertEquals(address.getZipCode(), addressDto.getZipCode());
    }

    @Test
    void shouldMapDtoToAddress() {
        // Given
        AddressDto addressDto = new AddressDto();
        addressDto.setStreet("Alte Post str 37");
        addressDto.setCity("Stuttgart");
        addressDto.setCountry("Deutschland");
        addressDto.setZipCode("34521");

        // When
        Address address = addressMapper.toEntity(addressDto);

        // Then
        assertEquals(addressDto.getStreet(), address.getStreet());
        assertEquals(addressDto.getCity(), address.getCity());
        assertEquals(addressDto.getCountry(), address.getCountry());
        assertEquals(addressDto.getZipCode(), address.getZipCode());
    }
}
