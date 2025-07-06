package com.oteller.hotelservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelDto {

    private Long id;

    @NotNull
    @NotBlank(message = "Hotel name is required")
    private String name;

    private int starRating;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @NotNull(message = "Address is required")
    @Valid
    private AddressDto addressDto;
}
