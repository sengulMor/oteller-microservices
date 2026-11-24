package com.oteller.hotelservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private Long id;
    @NotBlank(message = "Street is required")
    private String street;
    private Integer number;
    @NotBlank(message = "City is required")
    private String city;
    @NotBlank(message = "Country is required")
    private String country;
    @NotBlank(message = "Zip Code is required")
    private String zipCode;

    public AddressDto(Long id, String street, String city, String country, String zipCode) {
        this(id, street, null, city, country, zipCode);
    }
}
