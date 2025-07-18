package com.oteller.hotelservice.validation;

import com.oteller.hotelservice.dto.AddressDto;
import com.oteller.hotelservice.dto.HotelDto;
import com.oteller.hotelservice.model.Address;
import com.oteller.hotelservice.repository.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintValidatorContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UniqueHotelAddressValidatorTest {

    private AddressRepository addressRepository;
    private UniqueHotelAddressValidator validator;
    private ConstraintValidatorContext context;
    private ConstraintValidatorContext.ConstraintViolationBuilder builder;

    @BeforeEach
    void setUp() {
        addressRepository = mock(AddressRepository.class);
        validator = new UniqueHotelAddressValidator(addressRepository);
        context = mock(ConstraintValidatorContext.class);
        builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);

        when(context.buildConstraintViolationWithTemplate(any())).thenReturn(builder);
        when(builder.addPropertyNode(any())).thenReturn(mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class));
    }

    @Test
    void shouldReturnTrue_whenAddressIsNull() {
        HotelDto dto = new HotelDto();
        dto.setAddress(null);

        boolean isValid = validator.isValid(dto, context);

        assertTrue(isValid);
        verifyNoInteractions(addressRepository);
    }

    @Test
    void shouldReturnTrue_whenAddressNotExistsInDb() {
        AddressDto addressDto = getAddressDto(null);
        HotelDto dto = new HotelDto();
        dto.setAddress(addressDto);

        when(addressRepository.findByStreetAndCityAndZipCodeAndCountry(
                addressDto.getStreet(), addressDto.getCity(), addressDto.getZipCode(), addressDto.getCountry()))
                .thenReturn(Optional.empty());

        boolean isValid = validator.isValid(dto, context);

        assertTrue(isValid);
    }

    @Test
    void shouldReturnTrue_whenSameAddressWithMatchingIdExists() {
        AddressDto addressDto = getAddressDto(1L);
        HotelDto dto = new HotelDto();
        dto.setAddress(addressDto);

        Address addressEntity = new Address();
        addressEntity.setId(1L);

        when(addressRepository.findByStreetAndCityAndZipCodeAndCountry(
                addressDto.getStreet(), addressDto.getCity(), addressDto.getZipCode(), addressDto.getCountry()))
                .thenReturn(Optional.of(addressEntity));

        boolean isValid = validator.isValid(dto, context);

        assertTrue(isValid);
    }

    @Test
    void shouldReturnFalse_whenDuplicateAddressExistsWithDifferentId() {
        AddressDto addressDto = getAddressDto(1L);
        HotelDto dto = new HotelDto();
        dto.setAddress(addressDto);

        Address addressEntity = new Address();
        addressEntity.setId(99L); // different ID → conflict

        when(addressRepository.findByStreetAndCityAndZipCodeAndCountry(
                addressDto.getStreet(), addressDto.getCity(), addressDto.getZipCode(), addressDto.getCountry()))
                .thenReturn(Optional.of(addressEntity));

        boolean isValid = validator.isValid(dto, context);

        assertFalse(isValid);
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("{hotel.address.mustBeUnique}");
    }

    private AddressDto getAddressDto(Long id) {
        return AddressDto.builder()
                .id(id)
                .street("Main Street")
                .city("Istanbul")
                .zipCode("34000")
                .country("TR")
                .build();
    }
}
