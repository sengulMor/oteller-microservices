package com.oteller.hotelservice.repository;

import com.oteller.hotelservice.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    Optional<Hotel> findByAddressId(Long addressId);

    @Query("SELECT h FROM Hotel h JOIN FETCH h.rooms")
    List<Hotel> findAllWithRooms();                          // use this to avoid 1 + n problem
    // more performantanter as fetch.eager
    // only when you need the rooms also,
    // then is the this the best performant solution

}
