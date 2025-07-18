package com.oteller.hotelservice.validation;

import com.oteller.hotelservice.dto.RoomDto;
import com.oteller.hotelservice.repository.RoomRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
public class UniqueRoomValidator implements ConstraintValidator<UniqueRoom, RoomDto> {

    private final MessageSource messageSource;
    private final RoomRepository roomRepository;

    public UniqueRoomValidator(RoomRepository roomRepository, MessageSource messageSource) {
        this.roomRepository = roomRepository;
        this.messageSource = messageSource;
    }

    @Override
    public boolean isValid(RoomDto dto, ConstraintValidatorContext context) {
        if (dto == null) {
            return true;
        }

        if (isUnique(dto)) {
            return true;
        }

        String message = messageSource.getMessage(
                "room.number.mustBeUnique.forEachHotel",
                new Object[]{dto.getHotelId()},
                LocaleContextHolder.getLocale()
        );

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode("roomNumber")
                .addConstraintViolation();

        return false;
    }

    private boolean isUnique(RoomDto dto) {
        return roomRepository.findByHotel_IdAndRoomNumber(dto.getHotelId(), dto.getRoomNumber())
                .map(room -> Objects.equals(room.getId(), dto.getId()))
                .orElse(true);
    }

}
