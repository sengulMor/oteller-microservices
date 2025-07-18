package com.oteller.hotelservice.repository;

import com.oteller.hotelservice.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    Optional<Address> findByStreetAndCityAndZipCodeAndCountry(
            String street,
            String city,
            String zipCode,
            String country
    );

}
