package com.oteller.reservationservice.validation;

import com.oteller.reservationservice.dto.ReservationDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;


@Component
public class DateInRangeValidator implements ConstraintValidator<DateInRange, ReservationDTO> {

    @Override
    public boolean isValid(ReservationDTO dto, ConstraintValidatorContext context) {
        if (dto == null || dto.getCheckInDate() == null || dto.getCheckOutDate() == null) {
            return true;
        }
        if (dto.getCheckInDate().isBefore(dto.getCheckOutDate())) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("{checkInDate.must.before.checkOutDate}")
                .addPropertyNode("checkInDate")
                .addConstraintViolation();
        return false;
    }
}
