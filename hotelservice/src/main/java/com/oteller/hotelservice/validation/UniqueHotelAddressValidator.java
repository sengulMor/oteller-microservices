package com.oteller.hotelservice.validation;

import com.oteller.hotelservice.dto.AddressDto;
import com.oteller.hotelservice.dto.HotelDto;
import com.oteller.hotelservice.model.Address;
import com.oteller.hotelservice.repository.AddressRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class UniqueHotelAddressValidator implements ConstraintValidator<UniqueHotelAddress, HotelDto> {

    private final AddressRepository addressRepository;

    public UniqueHotelAddressValidator(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public boolean isValid(HotelDto dto, ConstraintValidatorContext context) {
        AddressDto addressDto = dto.getAddress();

        if (addressDto == null) {
            return true;
        }

        Optional<Address> optionalAddress = addressRepository.findByStreetAndCityAndZipCodeAndCountry(
                addressDto.getStreet(),
                addressDto.getCity(),
                addressDto.getZipCode(),
                addressDto.getCountry()
        );

        if (optionalAddress.isEmpty() || (addressDto.getId() != null && addressDto.getId().equals(optionalAddress.get().getId()))) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("{hotel.address.mustBeUnique}")
                .addPropertyNode("address")
                .addConstraintViolation();
        return false;
    }
}
