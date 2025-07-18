package com.oteller.hotelservice.validation;

import com.oteller.hotelservice.dto.RoomDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class ConditionalNotBlankValidator implements ConstraintValidator<ConditionalNotBlank, RoomDto> {
    @Override
    public boolean isValid(RoomDto dto, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (dto == null) return true;
        return dto.isAvailable()
                ? validateWhenAvailable(dto, context)
                : validateWhenUnavailable(dto, context);
    }

    private boolean validateWhenUnavailable(RoomDto dto, ConstraintValidatorContext context) {
        boolean valid = true;

        if (isBlank(dto.getGuestName())) {
            addViolation(context, "guestName", "{room.guestName.required}");
            valid = false;
        }

        if (dto.getCheckInDate() == null) {
            addViolation(context, "checkInDate", "{room.checkInDate.required}");
            valid = false;
        }

        if (dto.getCheckOutDate() == null) {
            addViolation(context, "checkOutDate", "{room.checkOutDate.required}");
            valid = false;
        }

        return valid;
    }

    private boolean validateWhenAvailable(RoomDto dto, ConstraintValidatorContext context) {
        boolean valid = true;

        if (!isBlank(dto.getGuestName())) {
            addViolation(context, "guestName", "{room.guestName.mustBeEmpty}");
            valid = false;
        }

        if (dto.getCheckInDate() != null) {
            addViolation(context, "checkInDate", "{room.checkInDate.mustBeEmpty}");
            valid = false;
        }

        if (dto.getCheckOutDate() != null) {
            addViolation(context, "checkOutDate", "{room.checkOutDate.mustBeEmpty}");
            valid = false;
        }

        return valid;
    }

    private void addViolation(ConstraintValidatorContext context, String field, String messageKey) {
        context.buildConstraintViolationWithTemplate(messageKey)
                .addPropertyNode(field)
                .addConstraintViolation();
    }

    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}


