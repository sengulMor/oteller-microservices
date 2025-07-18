package com.oteller.hotelservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address extends BaseEntity {

    @NotBlank(message = "Street must not be blank")
    @Column(name = "street", nullable = false, length = 255)
    private String street;

    @NotBlank(message = "City must not be blank")
    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @NotBlank(message = "Country must not be blank")
    @Column(name = "country", nullable = false, length = 100)
    private String country;

    @NotBlank(message = "Zip code must not be blank")
    @Column(name = "zip_code", nullable = false, length = 20)
    private String zipCode;

    @JsonIgnore
    @OneToOne(mappedBy = "address")
    private Hotel hotel;

}
