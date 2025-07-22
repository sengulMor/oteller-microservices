package com.oteller.hotelservice.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AddressDtoTest {

    @Test
    void testLombokGeneratedMethods() {
        AddressDto address = new AddressDto();
        address.setId(1L);
        address.setStreet("King street");
        address.setCity("London");
        address.setCountry("United Kingdom");
        address.setZipCode("34218");


        assertEquals("King street", address.getStreet());
        assertEquals(1L, address.getId());

        AddressDto builtAddress = AddressDto.builder()
                .id(2L)
                .street("Prince street")
                .city("Ulm")
                .country("Germany")
                .zipCode("98345")
                .build();

        assertEquals("Ulm", builtAddress.getCity());
        assertEquals("98345", builtAddress.getZipCode());
    }
}
