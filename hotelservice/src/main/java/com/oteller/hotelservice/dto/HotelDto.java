package com.oteller.hotelservice.dto;

import com.oteller.hotelservice.model.Room;
import com.oteller.hotelservice.validation.UniqueHotelAddress;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@UniqueHotelAddress
public class HotelDto {

    private Long id;

    @NotNull
    @NotBlank(message = "Hotel name is required")
    private String name;

    @Min(value = 1, message = "Star Rating can not smaller as 1")
    @Max(value = 5, message = "Star Rating can not bigger as 5")
    private int starRating;

    @NotNull(message = "Address is required")
    @Valid
    private AddressDto address;

    private List<Room> rooms;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
